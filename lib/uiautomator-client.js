/* ================================================================
 * uiautomator-client by xdf(xudafeng[at]126.com)
 *
 * first created at : Sun Feb 28 2016 20:34:26 GMT+0800 (CST)
 *
 * ================================================================
 * Copyright  xdf
 *
 * Licensed under the MIT License
 * You may not use this file except in compliance with the License.
 *
 * ================================================================ */

'use strict';

const net = require('net');
const path = require('path');
const _ = require('./helper');
const logger = require('./logger');

const READY_FLAG = _.uuid();
const FILE_NAME = 'uiautomator-bootstrap';
const CLASS_NAME = 'com.android.uiautomator.client.Initialize';
const binPath = path.join(__dirname, '..', 'bin', FILE_NAME + '.jar');

function UIAutomator() {
  this.adb = null;
  this.socket = null;
  this.socketPort = 6789;
  this.queue = [];
}

UIAutomator.prototype.init = function *(adb) {
  this.adb = adb;
  const ANDROID_TMP_DIR = this.adb.getTmpDir();

  try {
    yield this.adb.rm(`${ANDROID_TMP_DIR}/${FILE_NAME}.jar`);
  } catch (e) {

  }
  yield this.adb.push(binPath, ANDROID_TMP_DIR);
  try {
    yield this.adb.killProcess('uiautomator');
  } catch (e) {

  }
  yield this.adb.forward(this.socketPort, this.socketPort);
  yield this.initSocketServer();
  yield this.initSocketClient();
};

UIAutomator.prototype.initSocketClient = function() {
  return new Promise((resolve, reject) => {
    this.socket = net.connect(this.socketPort, () => {
      logger.info('socket client ready');
      resolve();
    });

    var tempData = '';
    this.socket.setEncoding('utf8');
    this.socket.on('data', data => {
      try {
        let res = JSON.parse(tempData + data);
        tempData = '';
        let success = res.success;

        if (success) {
          this.queue.shift().resolve(res.data);
        } else {
          throw res.errMsg;
        }
      } catch (e) {
        tempData += data;
      }
    });
    this.socket.on('end', () => {
      logger.warn('connect lost');
    });
  });
};

UIAutomator.prototype.initSocketServer = function() {
  return new Promise((resolve, reject) => {
    const ANDROID_TMP_DIR = this.adb.getTmpDir();
    let args = `shell uiautomator runtest ${ANDROID_TMP_DIR}/${FILE_NAME}.jar -c ${CLASS_NAME} -e port ${this.socketPort} -e flag ${READY_FLAG}`.split(' ');

    var proc = this.adb.spawn(args, {
      path: process.cwd(),
      env: process.env
    });

    proc.stderr.setEncoding('utf8');
    proc.stdout.setEncoding('utf8');
    proc.stdout.on('data', data => {
      if (!!~data.indexOf(READY_FLAG)) {
        logger.info('socket server ready');
        resolve();
      } else {
        console.log(data);
      }
    });
    proc.stderr.on('data', data => {
      console.log(data);
    });
  });
};

UIAutomator.prototype.send = function *(cmd) {
  var defer = new _.Defer();
  defer.promise.then(data => {
  });
  this.queue.push(defer);
  this.socket.write(`${JSON.stringify(cmd)}\n`);
  return yield defer.promise;
};

UIAutomator.fileName = FILE_NAME;

module.exports = UIAutomator;
