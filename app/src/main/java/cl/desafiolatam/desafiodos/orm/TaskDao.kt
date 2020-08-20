package cl.desafiolatam.desafiodos.orm

import androidx.room.*

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

    // Se crea la Query para actualizar
    // devemos pasarle el texto, asi con el SET le decimos cambia el campo name con el String que viene en la funcion
    // y que tega el id ... WHERE = DONDE uID es igual al que viene en la funcion
    @Query("UPDATE TaskEntity SET name = :name WHERE uId = :id")
        fun updateTaskByID(id:Int, name :String)
}