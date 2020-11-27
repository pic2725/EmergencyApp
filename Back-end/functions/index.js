const functions = require('firebase-functions');
const admin = require('firebase-admin');
const { randomBytes } = require('crypto');
admin.initializeApp();

// var serviceAccount = require("/emergency-c2c80-57b0b6f58df3.json");
// admin.initializeApp({
//     credential: admin.credential.cert(serviceAccount),
//     databaseURL: "https://emergency-c2c80.firebaseio.com"
// });

exports.sendEmergencyMessage = functions.https.onCall(async (data, context) => {

    const uid = context.auth.uid
    const time = new Date().toISOString()
    const latitudeString = data.latitude
    const longitudeString = data.longitude
    const latitude = parseFloat(latitudeString)
    const longitude = parseFloat(longitudeString)

    //Get called user data
    let userDataSnapshot = await admin.database().ref(`users/${uid}`).once("value")
    const userDAO = userDataSnapshot.val()
    const contactList = userDAO.contactList
    const emergencyMessage = userDAO.emergencyMessage
    const uuid = Math.random().toString(36).slice(2) + randomBytes(8).toString('hex') + new Date().getTime()

    console.info("Requested by " + uid)

    //Insert alert history
    await admin.database().ref(`users/${uid}/alertHistories/${uuid}`).set({
        time: time,
        latitude: latitude,
        longitude: longitude
    });

    //Process contact list
    contactList.forEach(async (item) => {
        let contactUserDataSanpshot = await admin.database().ref(`users`).orderByChild("phoneNumber").equalTo(`${item.phoneNumber}`).once("value");

        //When fail to find user, record failing to send push notification.
        if(contactUserDataSanpshot === null)
        {
            await admin.database().ref(`users/${uid}/alertHistories/${uuid}/contactedUserPhoneNumber`).push({
                name: "Unknown", 
                phoneNumber: targetPhoneNumber,
                method: 0,
                result: 1
            });
        }
        else
        {
            contactUserDataSanpshot.forEach(async (item) => {
                const targetUid = item.key
                const targetName = item.val().name
                const targetImageUrl = item.val().imageUrl
                const targetPhoneNumber = item.val().phoneNumber
                const targetFcmToken = item.val().fcmToken

                console.info("User searched : " + targetUid)

                //Record recevied history
                await admin.database().ref(`users/${targetUid}/alertReceivedHistories/${uuid}`).set({
                    time: time,
                    latitude: latitude,
                    longitude: longitude,
                    contactedUserPhoneNumber: {
                        name: userDAO.name, 
                        phoneNumber: userDAO.phoneNumber,
                        imageUrl: userDAO.imageUrl,
                        uid: uid,
                        method: 0
                    }
                });

                //Record sent historty
                await admin.database().ref(`users/${uid}/alertHistories/${uuid}/contactedUserPhoneNumber`).push({
                    name: targetName, 
                    phoneNumber: targetPhoneNumber,
                    imageUrl: targetImageUrl,
                    uid: targetUid,
                    method: 0,
                    result: 0
                })

                //Send push notification
                var message = {
                    data: {
                        time: time,
                        latitude: latitudeString.toString(),
                        longitude: longitudeString.toString(),
                        emergencyMessage: emergencyMessage,
                        name: userDAO.name,
                        phoneNumber: userDAO.phoneNumber,
                        imageUrl: userDAO.imageUrl,
                        uid: targetUid
                    },
                    android: {
                        priority: "high"
                    },
                    token: targetFcmToken
                    };

                let pushMessageResult = await admin.messaging().send(message);
                console.info("FCM Sent : " + pushMessageResult);
            });
        }
    });
});