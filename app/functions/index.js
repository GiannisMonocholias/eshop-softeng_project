const functions = require("firebase-functions");
const admin = require("firebase-admin");
const firebase_tools = require("firebase-tools");

admin.initializeApp();

exports.deleteCollection = functions.https.onCall(async (data, context) => {
    // Κάνουμε "έξυπνο" ξεπακετάρισμα:
    // Αν το Firebase τύλιξε το payload σε έξτρα data, το βρίσκουμε. Αλλιώς χρησιμοποιούμε το αρχικό.
    const payload = data.data ? data.data : data;
    const collectionPath = payload.collectionPath;

    // Προσθέτουμε console.log για να βλέπουμε στο Firebase Console τι ακριβώς έφτασε
    console.log("Λήψη δεδομένων από Android:", JSON.stringify(data));

    if (!collectionPath) {
        throw new functions.https.HttpsError('invalid-argument', 'Δεν δόθηκε το collectionPath.');
    }

    try {
        // Εκτέλεση της μαζικής διαγραφής από τον Server
        await firebase_tools.firestore.delete(collectionPath, {
            project: process.env.GCLOUD_PROJECT,
            recursive: true,
            yes: true,
            force: true // Αποτρέπει σφάλματα αν το collection είναι ήδη άδειο
        });

        // Επιστροφή επιτυχίας στο Android
        return { message: "Η διαγραφή του " + collectionPath + " ολοκληρώθηκε επιτυχώς!" };

    } catch (error) {
        console.error("Σφάλμα διαγραφής του " + collectionPath + ":", error);
        throw new functions.https.HttpsError('internal', 'Αποτυχία διαγραφής: ' + error.message);
    }
});