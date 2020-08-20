package cl.desafiolatam.desafiodos

import android.content.DialogInterface
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.desafiolatam.desafiodos.orm.TaskDao
import cl.desafiolatam.desafiodos.orm.TaskDatabase
import cl.desafiolatam.desafiodos.orm.TaskEntity
import cl.desafiolatam.desafiodos.task.OnItemClickListener
import cl.desafiolatam.desafiodos.task.TaskListAdapter
import cl.desafiolatam.desafiodos.task.TaskUIDataHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_task.view.*

class MainActivity : AppCompatActivity(), OnItemClickListener {
    override fun onItemClick(taskItem: TaskUIDataHolder) {
        val dialogView = layoutInflater.inflate(R.layout.add_task, null)
        val taskText = dialogView.task_input
        taskText.setText(taskItem.text)
        val dialogBuilder = AlertDialog
            .Builder(this)
            .setTitle("Editar una Tarea")
            .setView(dialogView)
            .setNegativeButton("Cerrar") {
                    dialog: DialogInterface, _: Int -> dialog.dismiss()}
            .setPositiveButton("Editar") {
                    _: DialogInterface, _: Int ->
                //generar código para editar/actualizar la tarea
                if (taskText.text?.isNotEmpty()!!) {
                    AsyncTask.execute{
                        // el metodo update que agregue al TaskDao
                        // que recibe el id y el texto a modificar
                        instanceDB.updateTaskByID(taskItem.id.toInt() , taskText.text.toString())
                        // despues hacemos el mismo proceso que antes de pasar la lista y que nos retorne un TaskUIDataHolder
                        // que pasamos al metodo updateData del adapter, como en las demas funsiones en que trabajamos con la BD
                        val newItems = createEntityListFromDatabase(instanceDB.getAllTask())
                        runOnUiThread{
                            adapter.updateData(newItems)

                        }
                    }
                    // Esto era para ver los id del taskItem que es un TaskUIDataHolder,
                    // que se pasan en el onBindViewHolder del adapter y se le agrego un Listener para saber cuando se hace click sobre el
                    //Toast.makeText(this, taskItem.id.toString(), Toast.LENGTH_LONG).show()
                }
            }
        dialogBuilder.create().show()
    }

    private lateinit var list: RecyclerView
    private lateinit var adapter: TaskListAdapter
    // crear las variables para utilizar la base de datos
    private lateinit var instanceDB: TaskDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setUpViews()
        //inicializar lo necesario para usar la base de datos
        instanceDB = TaskDatabase.getInstace(application).taskDao()
    }

    override fun onResume() {
        super.onResume()
        AsyncTask.execute {
            val newItems = createEntityListFromDatabase(instanceDB.getAllTask())
            Log.d("Datos", "$newItems")
            runOnUiThread {
                adapter.updateData(newItems)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when {
            item.itemId == R.id.add -> addTask()
            item.itemId == R.id.remove_all -> removeAll()
        }
        return true
    }

    private fun setUpViews() {
        list = task_list
        list.layoutManager = LinearLayoutManager(this)
        adapter = TaskListAdapter( mutableListOf(), this, this)
        list.adapter = adapter
    }

    private fun updateEntity(taskItem: TaskUIDataHolder, newText: String) {
        //completar método para actualizar una tarea en la base de datos
    }

    private fun addTask() {
        val dialogView = layoutInflater.inflate(R.layout.add_task, null)
        val taskText = dialogView.task_input
        val dialogBuilder = AlertDialog
            .Builder(this)
            .setTitle("Agrega una Tarea")
            .setView(dialogView)
            .setNegativeButton("Cerrar") {
                    dialog: DialogInterface, _: Int -> dialog.dismiss()}
            .setPositiveButton("Agregar") {
                    dialog: DialogInterface, _: Int ->
                if (taskText.text?.isNotEmpty()!!) {
                    //Completar para agregar una tarea a la base de datos
                    AsyncTask.execute{
                        instanceDB.insertTask(createEntity(taskText.text.toString()))
                        val newItems = createEntityListFromDatabase(instanceDB.getAllTask())

                        runOnUiThread{
                            adapter.updateData(newItems)
                            dialog.dismiss()
                        }
                    }
                }
            }
        dialogBuilder.create().show()
    }

    private fun removeAll() {
        val dialog = AlertDialog
            .Builder(this)
            .setTitle("Borrar Todo")
            .setMessage("¿Desea Borrar todas las tareas?")
            .setNegativeButton("Cerrar") {
                    dialog: DialogInterface, _: Int -> dialog.dismiss()}
            .setPositiveButton("Aceptar") { dialog: DialogInterface, _: Int ->
                //Código para eliminar las tareas de la base de datos
                AsyncTask.execute{
                    //var lista = instanceDB.getAllTask()
                    instanceDB.deleteAllTask2()
                    var lista = instanceDB.getAllTask()
                    var dataList = createEntityListFromDatabase(lista)
                    runOnUiThread{
                        adapter.updateData(dataList)
                    }
                }
            }
        dialog.show()
    }
    private fun createEntity(text:String): TaskEntity {
        //completar este método para retornar un Entity
        return TaskEntity(text)
    }

    private fun createEntityListFromDatabase(entities: List<TaskEntity>): MutableList<TaskUIDataHolder> {
        val dataList = mutableListOf<TaskUIDataHolder>()
        //completar método para crear una lista de datos compatibles con el adaptador, mire lo que
        //retorna el método. Este método debe recibir un parámetro también.
        if (entities.isNotEmpty()){
            for (entitie in entities){
                val dataView = TaskUIDataHolder(
                    entitie.uId!!,
                    entitie.name
                )
                dataList.add(dataView)
            }
        }
        return dataList
    }
}
