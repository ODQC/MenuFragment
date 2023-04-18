package cr.ac.menufragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment


/**
 * A simple [Fragment] subclass.
 * Use the [MyDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Crear un AlertDialog.Builder
        val builder = AlertDialog.Builder(requireActivity())

        // Configurar el diálogo
        builder.setTitle("Integrantes")
            .setMessage("Maikol Chinchilla, Oscar Quesada")
            .setNegativeButton("Cerrar") { dialog, which ->
                // Implementar la acción al hacer clic en el botón Cancelar
                // Aquí puedes poner el código que quieras ejecutar
                dialog.dismiss()
            }

        // Crear y devolver el AlertDialog
        return builder.create()
    }
}