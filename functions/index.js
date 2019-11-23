'use strict';
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
			} else {
				console.log("Notification Sent: ", result_3);
			}
		});
		return Promise.all(tokensToRemove);
	});


// //Forum Notification
// exports.forumNotification = functions.database.ref('/umumPos/{forumUid}/{umumUid}/{umumDetailUid}')
// 	.onWrite(async (change, context) => {

// 		//Get the value from the ref
// 		const umumUid = context.params.umumUid;

// 		//Get the value
// 		const snapShot = change.after.val();
// 		const deskripsi = snapShot.deskripsi;
// 		const senderUid = snapShot.registeredUid;

// 		// If nothing, we exit the function.
// 		if (!change.after.val()) {
// 			return console.log('User ', senderUid, 'nothing happened', deskripsi);
// 		}

// 		//Get the list of participants
// 		const getAllRegisteredParticipants = admin.database().ref(`/umumPosParticipants/${umumUid}/`).once('value');
// 		//Get the users details
// 		const getRegisteredUserProfilePromise = admin.database().ref(`/registeredUser/${senderUid}/`).once('value');

// 		//Make promise
// 		const resultFirst = await Promise.all([getAllRegisteredParticipants, getRegisteredUserProfilePromise]);
// 		let listAllRegisteredUser = resultFirst[0];
// 		const registeredUser = resultFirst[1];

// 		//Details
// 		const totalRegisteredUserInThatUmum = listAllRegisteredUser.numChildren();
// 		const listAllRegisteredUserUid = Object.keys(listAllRegisteredUser.val());


// 		// Check if there are any list of participants
// 		if (!listAllRegisteredUser.hasChildren()) {
// 			return console.log('There is no list of participant.');
// 		}
// 		// Check if there are any list of participants
// 		if (!registeredUser.hasChildren()) {
// 			return console.log('The user is not exist.');
// 		}

// 		console.log('There are', totalRegisteredUserInThatUmum, 'lists of participants in this Umum');
// 		console.log('Fetched follower profile', registeredUser);


// 		const username = registeredUser.val().username;
// 		console.log('He, ', username, ' is sending the message: ', deskripsi);

// 		//Create payload for the notification
// 		const payload = {
// 			notification: {
// 				title: username,
// 				body: deskripsi,
// 				icon: "default",
// 				sound: "default",
// 				click_action: "ForumSplash"
// 			}
// 		};

// 		console.log('All user uid: ', listAllRegisteredUserUid);

// 		var fcmPromise;
// 		let tokens;

// 		for (var y = 0; y < totalRegisteredUserInThatUmum; y++) {
// 			//Get the userUid
// 			const userUid = listAllRegisteredUserUid[y];
// 			if (senderUid !== userUid) {

// 				console.log('All user uid will receive : ', userUid, ' except : ', senderUid);
// 				fcmPromise = admin.database().ref(`/registeredUserTokenUid/${userUid}/`).once('value');
// 			}
// 		}

// 		// eslint-disable-next-line no-await-in-loop
// 		const results = await Promise.all([fcmPromise]);
// 		var snapTing;

// 		for (var x = 0; x < results.length; x++) {
// 			snapTing = results[x];
// 			if (!snapTing.hasChildren()) {
// 				return console.log('There is no list of tokens');
// 			}

// 			// Listing all tokens as an array.
// 			tokens = Object.keys(snapTing.val());

// 			tokens = tokens.concat(tokens); //adding all tokens of user to token array

// 			console.log("Tokens: " + tokens[x]);
// 		}

// 		// for (var i_1 in results) {

// 		// 	var usersTokenSnapShot = results[i_1];

// 		// 	if (usersTokenSnapShot !== null) {

// 		// 		if (!usersTokenSnapShot.hasChildren()) {
// 		// 			return console.log('There is no list of tokens');
// 		// 		}

// 		// 		if (usersTokenSnapShot.hasChildren()) {
// 		// 			const t = Object.keys(usersTokenSnapShot.val());
// 		// 			tokens = tokens.concat(t);
// 		// 			console.log("Tokens: " + tokens);
// 		// 		}
// 		// 	}
// 		// }

// 		const response = await admin.messaging().sendToDevice(tokens, payload);
// 		// For each message check if there was an error.
// 		const tokensToRemove = [];
// 		response.results.forEach((result_3, index) => {
// 			const error = result_3.error;
// 			if (error) {
// 				console.error('Failure sending notification to uid=', tokens[index], error);
// 				// Cleanup the tokens who are not registered anymore.
// 				if (error.code === 'messaging/invalid-registration-token' || error.code === 'messaging/registration-token-not-registered') {
// 					tokensToRemove.push(usersTokenSnapShot.ref.child(tokens[index]).remove());
// 				}
// 			} else {
// 				console.log("Notification Sent: ", result_3);
// 			}
// 		});
// 		return Promise.all(tokensToRemove);
// 	});


///Admin Group Notification
exports.forumNotification2 = functions.region("asia-east2").database.ref('/umumPos/{forumUid}/{umumUid}/{umumDetailUid}')
	.onCreate(async (snap, context) => {
		//Get the value from the ref
		const umumUid = context.params.umumUid;
		//Get the value
		const snapShot = snap.val();
		const senderUid = snapShot.registeredUid;
		const message = snapShot.deskripsi;

		//Retrieve all adminsUid which are subsribed to channel 1
		const getAllSubscribersPromise = admin.database().ref(`/umumPosParticipants/${umumUid}/`).once('value');

		//Getting admin name
		const getAdminPromise = admin.database().ref(`/registeredUser/${senderUid}/`).once('value');
		const result = await getAdminPromise;
		//Get fullName and type of the admin
		const fullName = result.val().username;
		const type = result.val().email;

		console.log("umumUid: " + umumUid + " | SenderUid: " + senderUid);

		//Create payload for the notification
		const payload = {
			notification: {
				title: fullName,
				subtitle: type,
				body: message,
				sound: "default",
				icon: "default",
				click_action: "ForumSplash"
			}
		};

		//results will have children having keys of subscribers uid.
		const userUidSnapShot = await getAllSubscribersPromise;

		//Check if there is children or not
		if (!userUidSnapShot.hasChildren()) {
			return console.log('There are no subscribed users to write notifications.');
		}
		//Get the value total admin will receive the notification
		const totalAdmin = userUidSnapShot.numChildren();
		console.log('Total User in that Umum detail: ' + totalAdmin);
		//Get the values of adminUid
		const totalAdminValue = Object.keys(userUidSnapShot.val()); //fetched the keys creating array of subscribed users

		console.log('totalAdminValue: ' + userUidSnapShot.val());
		var AllFollowersFCMPromises = []; //create new array of promises of TokenList for every subscribed users

		for (var i = 0; i < totalAdmin; i++) {
			//Get the adminUid
			const adminUid = totalAdminValue[i];
			console.log('User ' + i + " : uid: " + adminUid);

			AllFollowersFCMPromises[i] = admin.database().ref(`/registeredUserTokenUid/${adminUid}/`).once('value');
		}

		const results = await Promise.all(AllFollowersFCMPromises);
		// here is created array of tokens now ill add all the fcm tokens of all the user and then send notification to all these.
		var tokens = [];
		for (var i_1 in results) {
			var usersTokenSnapShot = results[i_1];
			console.log("usersTokenSnapShot: " + usersTokenSnapShot);
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