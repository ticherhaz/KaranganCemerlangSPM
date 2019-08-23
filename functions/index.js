const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

//HubungiKami Notification
exports.hubungiKamiChatNotification = functions.database.ref("hubungiKamiMessage/{senderUid}/{adminUid}")
	.onWrite(async (change, context) => {

		//Get the value from the ref
		const senderUid = context.params.senderUid;
		const adminUid = context.params.adminUid;

		//Get the value
		const snapShot = change.after.val();
		const message = snapShot.message;
		const chatUid = snapShot.chatUid;

		console.log(senderUid);

		//Retrieve all adminsUid which are chatting at livechatChatUid
		const getAllSubscribersPromise = admin.database().ref("hubungiKamiParticipant/" + chatUid + "/").once('value');

		//Getting user name
		const getAdminPromise = admin.database().ref("registeredUser/" + senderUid).once('value');
		const result = await getAdminPromise;
		//Get username of the admin
		const username = result.val().username;
		//Create payload for the notification
		const payload = {
			notification: {
				title: username,
				body: message,
				icon: "default",
				sound: "default",
				click_action: "HubungiKami"
			}
		};

		//results will have children having keys of subscribers uid.
		const userUidSnapShot = await getAllSubscribersPromise;

		//Check if there is children or not
		if (!userUidSnapShot.hasChildren()) {
			return console.log('There are no subscribed users to write notifications.');
		}
		//Get the value total admin will receive the notification
		const totalRegisteredUser = userUidSnapShot.numChildren();
		//Get the values of userUid
		const totalUserValue = Object.keys(userUidSnapShot.val()); //fetched the keys creating array of subscribed users
		var AllFollowersFCMPromises = []; //create new array of promises of TokenList for every subscribed users
		for (var i = 0; i < totalRegisteredUser; i++) {
			//Get the userUid
			const userUid = totalUserValue[i];
			//Check if the userUid is same as sender
			if (userUid !== senderUid) {
				AllFollowersFCMPromises[i] = admin.database().ref("registeredUserTokenUid/" + userUid).once('value');
			}
		}
		const results = await Promise.all(AllFollowersFCMPromises);
		// here is created array of tokens now ill add all the fcm tokens of all the user and then send notification to all these.
		var tokens = [];
		for (var i_1 in results) {
			var usersTokenSnapShot = results[i_1];
			if (usersTokenSnapShot !== null) {
				if (usersTokenSnapShot.hasChildren()) {
					const t = Object.keys(usersTokenSnapShot.val()); //array of all tokens of user [n]
					tokens = tokens.concat(t); //adding all tokens of user to token array
				}
			}
		}
		const response = await admin.messaging().sendToDevice(tokens, payload);
		// For each message check if there was an error.
		const tokensToRemove = [];
		response.results.forEach((result_3, index) => {
			const error = result_3.error;
			if (error) {
				console.error('Failure sending notification to uid=', tokens[index], error);
				// Cleanup the tokens who are not registered anymore.
				if (error.code === 'messaging/invalid-registration-token' || error.code === 'messaging/registration-token-not-registered') {
					tokensToRemove.push(usersTokenSnapShot.ref.child(tokens[index]).remove());
				}
			}
			else {
				console.log("Notification Sent: ", result_3);
			}
		});
		return Promise.all(tokensToRemove);
	});