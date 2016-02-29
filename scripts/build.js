#!/usr/bin/env node

/* ================================================================
 * uiautomator-client by xdf(xudafeng[at]126.com)
 *
 * first created at : Wed Aug 26 2015 11:55:14 GMT+0800 (CST)
 *
 * ================================================================
 * Copyright  xdf
 *
 * Licensed under the MIT License
 * You may not use this file except in compliance with the License.
 *
 * ================================================================ */

'use strict';

var fs = require('fs');
var path = require('path');
var _ = require('../lib/helper');
var spawn = require('win-spawn');
var JAVA_HOME = require('java-home');
var ant = require('ant-lite').binPath;
var fileName = require('..').fileName;

var isWindows = _.platform.isWindows;
var cwd = path.join(__dirname, '..');

function selectAndroidSdkSync() {
  var env = global.process.env;

  if (!env.ANDROID_HOME) {
    //throw 'ANDROID_HOME is not set';
    console.log('ANDROID_HOME is not set');
    return null;
  }

  var platforms = path.join(env.ANDROID_HOME, 'platforms');

  if (!_.isExistedDir(platforms)) {
    console.log('platforms directory is not exist');
    return null;
  }

  var res = fs.readdirSync(platforms);

  res = _.filter(res, n => {
    return !!~n.indexOf('android');
  });

  if (!res.length) {
    console.log('platforms directory is not exist');
    return null;
  }

  return res;
}

var buildBootstrap = callback => {

  var process = spawn(ant, ['build'], {
    cwd: cwd
  });
  process.on('error', err => {
    //throw err;
    console.log(err);
  });

  process.stdout.setEncoding('utf8');
  process.stderr.setEncoding('utf8');

  process.stdout.on('data', data => {
    console.log(data);
  });
  process.stderr.on('data', data => {
    console.log(data);
  });

  process.on('exit', code => {
    if (code !== 0) {
      //throw 'build failed';
      console.log('build failed');
    }
    callback();
  });

};

JAVA_HOME.getPath((error, javaHome) => {
  if (error) {
    //throw 'JAVA_HOME is not set';
    console.log('JAVA_HOME is not set');
  }
  console.log('JAVA_HOME is set to ' + javaHome);

  var env = global.process.env;

  if (!env.ANDROID_HOME) {
    //throw 'ANDROID_HOME is not set';
    console.log('ANDROID_HOME is not set');
    return;
  }

  var android = isWindows ? 'android.bat' : 'android';
  var androidTool = path.resolve(env.ANDROID_HOME, 'tools', android);

  if (!_.isExistedFile(androidTool)) {
    //throw '`android` command was not found';
    console.log('`android` command was not found');
  }

  var sdkVersion = selectAndroidSdkSync();

  if (!sdkVersion) {
    console.log('not avaliable sdk');
    return;
  }

  sdkVersion = sdkVersion[sdkVersion.length - 1];

  var args = ['create', 'uitest-project', '-n', fileName, '-t', sdkVersion, '-p', '.'];

  var process = spawn(androidTool, args, {
    cwd: cwd
  });

  process.on('error', err => {
    //throw err;
    console.log(err);
  });

  process.stdout.setEncoding('utf8');
  process.stderr.setEncoding('utf8');
  process.stdout.on('data', data => {
    console.log(data);
  });
  process.stderr.on('data', data => {
    console.log(data);
  });
  process.on('exit', code => {
    if (code !== 0) {
      //throw 'setup failed';
      console.log('setup failed');
    }

    buildBootstrap(() => {
      console.log(fileName + ' build success!');
    });
  });

});
