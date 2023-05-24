package com.mus.mynotes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.mus.mynotes.dal.local.AppDatabase
import com.mus.mynotes.dal.local.NotesDAO
import com.mus.mynotes.models.notes.NotesModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class NotesDAOTest {

    // Added this rule as we need to tell JUnit we need to run the code async - one by one - in same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: AppDatabase
    private lateinit var dao: NotesDAO

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.notesDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertNoteItem(): Unit = runTest {
        val noteItem = NotesModel(
            id = 1,
            title = "test",
            description = "test description",
            timestamp = null,
            color = "#000000"
        )
        dao.insertNote(noteItem)
        val notesItems = dao.observeNotesItems().getOrAwaitValue()
        assertThat(notesItems).contains(noteItem)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteNoteItem(): Unit = runTest {
        val noteItem = NotesModel(
            id = 1,
            title = "test",
            description = "test description",
            timestamp = null,
            color = "#000000"
        )
        dao.insertNote(noteItem)
        dao.deleteNote(noteId = 1)
        val notesItems = dao.observeNotesItems().getOrAwaitValue()
        assertThat(notesItems).doesNotContain(noteItem)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun updateNoteItem(): Unit = runTest {
        val noteItem = NotesModel(
            id = 1,
            title = "test",
            description = "test description",
            timestamp = null,
            color = "#000000"
        )
        dao.insertNote(noteItem)
        val noteItemUpdated = NotesModel(
            id = 1,
            title = "test updated",
            description = "test description updated",
            timestamp = null,
            color = "#ffffff"
        )
        dao.updateNote(noteItemUpdated)
        val notesItems = dao.observeNotesItems().getOrAwaitValue()
        assertThat(notesItems).contains(noteItemUpdated)
    }
}