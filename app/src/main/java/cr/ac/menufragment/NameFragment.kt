import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import cr.ac.menufragment.NameChangeListener
import cr.ac.menufragment.NameService
import cr.ac.menufragment.R

class NameFragment :Fragment() {
    private lateinit var etName: EditText
    private lateinit var btnUpdate: Button

    private var nameService: NameService? = null // Variable para mantener la referencia al servicio
    private var isBound = false // Variable para verificar si el servicio está vinculado

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val binder = iBinder as NameService.MyBinder
            nameService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            isBound = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_name, container, false)

        // Vincula el servicio al Fragment
        val intent = Intent(requireContext(), NameService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        etName = view.findViewById(R.id.editTextName)
        btnUpdate = view.findViewById(R.id.buttonUpdate)
        etName.setOnFocusChangeListener { _, hasFocus ->
            // Verifica si ha perdido el enfoque
            if (!hasFocus) {
                // Cierra el teclado

                closeKeyboard()
            }
        }
        btnUpdate.setOnClickListener {
                closeKeyboard()

            // Actualiza el nombre en el servicio cuando se hace clic en el botón
            if (isBound) {
                val newName = etName.text.toString()
             //   Log.d("NameService", "El valor de name ha cambiado a: $newName")
                nameService?.setName(newName)
            }
        }

        return view
    }
    // Función para cerrar el teclado
    fun closeKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Desvincula el servicio del Fragment al destruir la vista del Fragment
        requireContext().unbindService(serviceConnection)
        isBound = false
    }
}