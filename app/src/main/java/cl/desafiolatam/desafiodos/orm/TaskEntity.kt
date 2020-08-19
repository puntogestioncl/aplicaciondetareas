package cl.desafiolatam.desafiodos.orm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(val name: String = "name") {

    @PrimaryKey(autoGenerate = true)
    var uId:Long? = null

}