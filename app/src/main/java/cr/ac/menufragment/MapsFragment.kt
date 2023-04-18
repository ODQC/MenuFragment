package cr.ac.menufragment

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class MapsFragment : Fragment() {
    private var nameService: NameService? = null // Variable para mantener la referencia al servicio
    private var locationsService: LocationsService? = null // Variable para mantener la referencia al servicio
    private var isBound = false // Variable para verificar si el servicio está vinculado
    private var isBound2 = false // Variable para verificar si el servicio está vinculado
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLatLng: LatLng

    private lateinit var btnAdd: Button

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
    private val serviceConnection2 = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val binder = iBinder as LocationsService.MyBinder
            locationsService = binder.getService()
            isBound2 = true
        }
        override fun onServiceDisconnected(componentName: ComponentName) {
            isBound2 = false
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el diseño del Fragmento en la vista
        val view = inflater.inflate(R.layout.fragment_maps, container, false)


        val intent = Intent(requireContext(), NameService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        val intent2 = Intent(requireContext(), LocationsService::class.java)
        requireContext().bindService(intent2, serviceConnection2, Context.BIND_AUTO_CREATE)


        // Encuentra el botón por su ID
        val addButton = view.findViewById<Button>(R.id.btnAddMarker)
        addButton.setOnClickListener {
            // Llama al método addMarkerNearCurrentLocation() cuando se hace clic en el botón
            addMarkerNearCurrentLocation()
        }
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync{
                googleMap ->
                map = googleMap
                getLocation()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                // Ubicación obtenida con éxito
                if (location != null) {
                    Log.d("NameService", "El valor de name ha cambiado a: $nameService?.getName()")
                    currentLatLng = LatLng(location.latitude, location.longitude)
                   if(nameService?.getName()!=null){
                       map.addMarker(
                           MarkerOptions().position(currentLatLng).title(nameService?.getName())
                       )
                   }else {
                       map.addMarker(
                           MarkerOptions().position(currentLatLng).title("Ubicación actual")
                       )
                   }
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
                loadMarkersFromGeneratedLocations()
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    getLocation()
                }
            } else {
                // Permiso denegado, maneja la situación de acuerdo a tus necesidades
            }
        }
    }
    fun addMarkerNearCurrentLocation() {
        val random = Random()
        // Genera un offset aleatorio en la latitud y longitud para agregar el marcador cerca de la ubicación actual
        val latOffset = (random.nextDouble() - 0.5) / 100 // Offset de latitud de +/- 0.005 grados (equivalente a aproximadamente 500 metros)
        val lngOffset = (random.nextDouble() - 0.5) / 100 // Offset de longitud de +/- 0.005 grados (equivalente a aproximadamente 500 metros)
        val newLatLng = LatLng(currentLatLng.latitude + latOffset, currentLatLng.longitude + lngOffset)
        currentLatLng = newLatLng
        // Agrega el marcador en la nueva ubicación generada
        map.addMarker(
            MarkerOptions().position(newLatLng).title(nameService?.getName())
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

       locationsService!!.addLocation(newLatLng)
    }
    fun loadMarkersFromGeneratedLocations() {
        var lis= locationsService!!.getList()
        if(lis != null){
            for (location in lis) {
                map.addMarker(
                    MarkerOptions().position(location).title(nameService?.getName())
                )
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Desvincula el servicio del Fragment al destruir la vista del Fragment
        requireContext().unbindService(serviceConnection)
        isBound = false
    }
}