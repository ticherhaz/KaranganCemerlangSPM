/**
 * Triggers when a user gets a new follower and sends a notification.
 *
 * Followers add a flag to `/followers/{followedUid}/{followerUid}`.
 * Users save their device notification tokens to `/users/{followedUid}/notificationTokens/{notificationToken}`.
 */
exports.sendFollowerNotification = functions.database.ref('/followers/{followedUid}/{followerUid}')
  .onWrite(async (change, context) => {
    const followerUid = context.params.followerUid;
    const followedUid = context.params.followedUid;
    // If un-follow we exit the function.
    if (!change.after.val()) {
      return console.log('User ', followerUid, 'un-followed user', followedUid);
    }
    console.log('We have a new follower UID:', followerUid, 'for user:', followedUid);

    // Get the list of device notification tokens.
    const getDeviceTokensPromise = admin.database()
      .ref(`/users/${followedUid}/notificationTokens`).once('value');

    // Get the follower profile.
    const getFollowerProfilePromise = admin.auth().getUser(followerUid);

    // The snapshot to the user's tokens.
    let tokensSnapshot;

    // The array containing all the user's tokens.
    let tokens;

    const results = await Promise.all([getDeviceTokensPromise, getFollowerProfilePromise]);
    tokensSnapshot = results[0];
    const follower = results[1];

    // Check if there are any device tokens.
    if (!tokensSnapshot.hasChildren()) {
      return console.log('There are no notification tokens to send to.');
    }
    console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
    console.log('Fetched follower profile', follower);

    // Notification details.
    const payload = {
      notification: {
        title: 'You have a new follower!',
        body: `${follower.displayName} is now following you.`,
        icon: follower.photoURL
      }
    };

    // Listing all tokens as an array.
    tokens = Object.keys(tokensSnapshot.val());
    // Send notifications to all tokens.
    const response = await admin.messaging().sendToDevice(tokens, payload);
    // For each message check if there was an error.
    const tokensToRemove = [];
    response.results.forEach((result, index) => {
      const error = result.error;
      if (error) {
        console.error('Failure sending notification to', tokens[index], error);
        // Cleanup the tokens who are not registered anymore.
        if (error.code === 'messaging/invalid-registration-token' ||
          error.code === 'messaging/registration-token-not-registered') {
          tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
        }
      }
    });
    return Promise.all(tokensToRemove);
  });

//Admin Group Notification
exports.forumNotification2 = functions.region("asia-east2").database.ref('/umumPos/{forumUid}/{umumUid}/{umumDetailUid}')
.onCreate(async (snap, context) => {

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

  console.log("Type: " + type);

  //Create payload for the notification
  const payload = {
    notification: {
      title: fullName,
      subtitle: type,
      body: message,
      sound: "default",
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
  //Get the values of adminUid
  const totalAdminValue = Object.keys(userUidSnapShot.val()); //fetched the keys creating array of subscribed users
  var AllFollowersFCMPromises = []; //create new array of promises of TokenList for every subscribed users
  for (var i = 0; i < totalAdmin; i++) {
    //Get the adminUid
    const adminUid = totalAdminValue[i];
    //Check if the adminUid is same as sender
    if (adminUid !== senderUid) {
      AllFollowersFCMPromises[i] = admin.database().ref(`/registeredUserTokenUid/${adminUid}/`).once('value');
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