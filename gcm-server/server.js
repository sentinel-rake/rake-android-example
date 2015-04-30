var gcm = require('node-gcm');

// or with object values
var message = new gcm.Message({
    data: {
        title: 'GCM Hello World',
        message: 'GCM Message Here'
    }
});

// set your access_key, device regId;
var server_access_key = '';
var registration_id = '';

var registrationIds = [];
registrationIds.push(registration_id);

var sender = new gcm.Sender(server_access_key);

sender.send(message, registrationIds, 4, function (err, result) {
    console.log(result);
});
