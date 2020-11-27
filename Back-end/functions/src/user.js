const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

/*** Auth Callback Events ***/
exports.registered = functions.auth.user().onCreate((user) => {
    const email = user.email; // The email of the user.
    const displayName = user.displayName; // The display name of the user.

    console.log(`${displayName}(${email}) Registered.`)
});
 
/*** Auth Callback Events ***/
exports.unregistered = functions.auth.user().onDelete((user) => {
    const email = user.email; // The email of the user.
    const displayName = user.displayName; // The display name of the user.

    console.log(`${displayName}(${email}) Unregistered.`)
});