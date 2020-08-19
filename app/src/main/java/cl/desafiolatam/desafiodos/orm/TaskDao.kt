package cl.desafiolatam.desafiodos.orm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {

    @Query("SELECT * FROM TaskEntity")
    fun getAllTask() : List<TaskEntity>

    @Insert
    fun insertTask(tarea: TaskEntity)

    @Delete
    fun deleteAllTask(lista: List<TaskEntity>)

    @Query("DELETE FROM TaskEntity")
    fun deleteAllTask2()
}