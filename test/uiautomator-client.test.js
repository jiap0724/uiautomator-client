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

var ADB = require('macaca-adb');
var UIAutomator = require('..');

describe('android runtime socket protocol', function() {

  var client = new UIAutomator();

  it('init uiautomator', function *(done) {
    var adb = new ADB();
    var devices = yield ADB.getDevices();

    if (!devices.length) {
      done();
    }

    var device = devices[0];
    adb.setDeviceId(device.udid);
    yield client.init(adb);
    done();
  });

  it('ping should be ok', function *() {
    yield client.send({
      cmd: 'ping',
      args: {}
    });
  });
});
