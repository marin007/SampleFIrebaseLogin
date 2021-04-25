import * as functions from "firebase-functions";

// The Firebase Admin SDK to access Firestore.
const admin = require('firebase-admin');
admin.initializeApp();

// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript
//
// export const helloWorld = functions.https.onRequest((request, response) => {
//   functions.logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });


exports.makeUppercase = functions.firestore.document('/items/{documentId}')
  .onCreate((snap, context) => {
    // Grab the current value of what was written to Firestore.
    var original = snap.data().original.description;

    // Access the parameter `{documentId}` with `context.params`
    functions.logger.log('Uppercasing', context.params.documentId, original);
    const uppercase = makeFirstLetteCapital(original);
    return snap.ref.set({ uppercase }, { merge: true });
  });

exports.trimEmptySpace = functions.firestore.document('/items/{documentId}')
  .onCreate((snap, context) => {
    // Grab the current value of what was written to Firestore.
    var original = snap.data().original.description;

    // Access the parameter `{documentId}` with `context.params`
    functions.logger.log('Trimming empty spaces', context.params.documentId, original);
    const trimmedDescription = original.trim();
    return snap.ref.set({ trimmedDescription }, { merge: true });
  });

exports.removeContiniusSpaces = functions.firestore.document('/items/{documentId}')
  .onCreate((snap, context) => {
    // Grab the current value of what was written to Firestore.
    var original = snap.data().original.description;

    // Access the parameter `{documentId}` with `context.params`
    functions.logger.log('Remove continius spaces', context.params.documentId, original);
    //remove continius spaces between words
    const trimmedDescription = original.replace(/^\s+|\s+$/g, "");
    return snap.ref.set({ trimmedDescription }, { merge: true });
  });


function makeFirstLetteCapital(word: string) {
  return word.charAt(0).toUpperCase() + word.slice(1);
}
