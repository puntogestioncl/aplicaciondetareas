package cl.desafiolatam.desafiodos.orm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object{

        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getInstace(context: Context): TaskDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room
                        .databaseBuilder(context.applicationContext, TaskDatabase::class.java, "task_database")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}