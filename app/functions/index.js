const functions = require("firebase-functions");
const admin = require("firebase-admin");
const firebase_tools = require("firebase-tools");

admin.initializeApp();

exports.deleteCollection = functions.https.onCall(async (data, context) => {
    // Ελέγχουμε αν υπάρχει το collectionPath στα δεδομένα που στείλαμε
    if (!data.collectionPath) {
        throw new functions.https.HttpsError('invalid-argument', 'Δεν δόθηκε το collectionPath.');
    }

    const collectionPath = data.collectionPath;

    try {
        // Εκτέλεση της μαζικής διαγραφής από τον Server
        await firebase_tools.firestore.delete(collectionPath, {
            project: process.env.GCLOUD_PROJECT,
            recursive: true,
            yes: true
        });

        // Επιστροφή επιτυχίας στο Android
        return { message: "Η διαγραφή του " + collectionPath + " ολοκληρώθηκε επιτυχώς!" };
        
    } catch (error) {
        console.error("Σφάλμα διαγραφής:", error);
        throw new functions.https.HttpsError('internal', 'Αποτυχία διαγραφής: ' + error.message);
    }
});