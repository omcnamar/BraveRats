package com.olegmcnamara.braverats.remote.database

import com.google.firebase.database.*
import java.lang.Exception

enum class EventType {
    childAdded,
    childDeleted,
    childChanged,
    childMoved,
    canceledError
}

class FirebaseDatabaseHelper<T>(tableName: String,
                                valueType: Class<T>) {

    private val mTable = FirebaseDatabase.getInstance().getReference(tableName)
    private val type = valueType

    fun insert(row: Any,
               success: (String) -> Unit,
               failure: (Exception) -> Unit){

        val id = mTable.push().key
        id?.let {
            mTable.child(it).setValue(row).addOnCompleteListener{ task ->

                if (task.isSuccessful) {
                    // add id to the newly created row
                    update(arrayListOf(it, "id"), it, {}, failure)
                    success(it)

                } else {
                    task.exception?.let {
                        failure(it)
                    }
                }
            }
        }
    }

    fun insertWithId(row: Any,
                     id: String,
                     success: () -> Unit,
                     failure: (Exception) -> Unit) {

        mTable.child(id).setValue(row).addOnCompleteListener{ task ->

            if (task.isSuccessful) {
                // add id to the newly created row
                update(arrayListOf(id, "id"), id, success, failure)
            } else {
                task.exception?.let {
                    failure(it)
                }
            }
        }
    }

    fun update(path: ArrayList<String>,
               newValue: Any,
               success: () -> Unit,
               failure: (Exception) -> Unit
    ) {

        var ref = mTable
        for (part in path) {
            ref = ref.child(part)
        }
        ref.setValue(newValue).addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                success()
            } else {
                task.exception?.let {
                    failure(it)
                }
            }
        }

    }

    fun delete(path: ArrayList<String>,
               success: () -> Unit,
               failure: (Exception) -> Unit
    ) {

        var ref = mTable
        for (part in path) {
            ref = ref.child(part)
        }
        ref.setValue(null).addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                success()
            } else {
                task.exception?.let {
                    failure(it)
                }
            }
        }

    }

    fun addDataChangedListener(listener: (T, EventType) -> Unit) {

        mTable.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {
                val data = dataSnapshot.getValue(type)
                data?.let {
                    listener(it, EventType.childMoved)
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                val data = dataSnapshot.getValue(type)
                data?.let {
                    listener(it, EventType.childChanged)
                }
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val data = dataSnapshot.getValue(type)
                data?.let {
                    listener(it, EventType.childAdded)
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue(type)
                data?.let {
                    listener(it, EventType.childDeleted)
                }
            }
        })
    }
}