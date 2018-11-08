#!/usr/bin/env node
var prerender = require('./lib');

var server = prerender({
    chromeLocation: '/usr/bin/chromium',
    pageLoadTimeout: 60* 1000,
    waitAfterLastRequest: 60* 1000,
    followRedirects: true
});

server.use(prerender.sendPrerenderHeader());
// server.use(prerender.blockResources());
server.use(prerender.removeScriptTags());
server.use(prerender.httpHeaders());
server.use(prerender.removeStyle());
server.use(prerender.removeAngularjs());

server.start();
