package com.example.notesapp.repository

import com.example.notesapp.models.Notes
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val NOTES_COLLECTION_REF = "notes"

class StorageRepo {

    fun user() = Firebase.auth.currentUser
    val hasUser = Firebase.auth.currentUser != null


    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()
    private val notesRef = Firebase.firestore.collection(NOTES_COLLECTION_REF)

    fun getUserNotes(userId: String): Flow<Resource<List<Notes>>> = callbackFlow {
        var snapshotListener: ListenerRegistration? = null
        try {
            snapshotListener = notesRef
                .orderBy("timestamp")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val notes = snapshot.toObjects(Notes::class.java)
                        Resource.Success(data = notes)
                    } else {
                        Resource.Error(throwable = e?.cause)
                    }
                    trySend(response)
                }


        } catch (e: Exception) {
            trySend(Resource.Error(e?.cause))
            e.printStackTrace()
        }
        awaitClose() {
            snapshotListener?.remove()
        }
    }

    fun getNote(
        noteId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Notes?) -> Unit
    ) {
        notesRef
            .document(noteId)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it.toObject(Notes::class.java))
            }
            .addOnFailureListener {
                onError.invoke(it.cause)
            }
    }

    fun addNote(
        userId: String,
        title: String,
        description: String,
        timeStamp: Timestamp,
        color: Int = 0,
        onComplete: (Boolean) -> Unit
    ) {
        val documentId = notesRef.document().id
        val note = Notes(
            userId,
            title,
            description,
            timeStamp,
            color,
            documentId
            )
        notesRef.document(documentId).set(note)
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }
    fun deleteNote(noteId:String,onComplete: (Boolean) -> Unit){
        notesRef.document(noteId).delete().addOnCompleteListener {
            onComplete.invoke(it.isSuccessful)
        }
    }
    fun updateNote(
        title:String,
        description: String,
        color :Int,
        noteId: String,
        onResult:(Boolean)->Unit
    ){
        val updateData= hashMapOf<String,Any>(
            "colorIndex" to color,
            "title" to title,
            "description" to description,
        )
        notesRef.document(noteId).update(updateData).addOnCompleteListener {
            onResult.invoke(it.isSuccessful)
        }

    }
    fun signOut()  = Firebase.auth.signOut()
}


sealed class Resource<T>(
    val data: T? = null,
    val throwable: Throwable? = null
) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T?) : Resource<T>(data = data)
    class Error<T>(throwable: Throwable?) : Resource<T>(throwable = throwable)
}

