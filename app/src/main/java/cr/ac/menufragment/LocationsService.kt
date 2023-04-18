package cr.ac.menufragment

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.IBinder
import com.google.android.gms.maps.model.LatLng

class LocationsService : Service() {
    private val generatedLocations: MutableList<LatLng> = mutableListOf()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Aquí puedes realizar cualquier inicialización o lógica que necesites al iniciar el servicio
        return START_STICKY // Esto indica que el servicio debe seguir ejecutándose aunque la app sea cerrada
    }

    override fun onBind(intent: Intent?): IBinder? {
        return MyBinder() // Retorna una instancia de MyBinder para que los componentes puedan interactuar con el servicio
    }


    fun addLocation(location: LatLng) {
        this.generatedLocations.add(location)
    }

    fun getList(): MutableList<LatLng> {
        return generatedLocations // Método para obtener el valor de la variable name
    }

    inner class MyBinder : Binder() {
        fun getService(): LocationsService {
            return this@LocationsService
        }
    }
}