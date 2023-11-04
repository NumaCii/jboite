import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.jboite.R

data class Sound(val name: String, val uri: String)

class MainActivity : AppCompatActivity() {

    private lateinit var sounds: MutableList<Sound>
    private var selectedSoundUri: Uri? = null

    companion object {
        private const val REQUEST_CODE_PICK_SOUND = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisez votre liste de sons ici
        sounds = mutableListOf(
            Sound("Sound 1", "android.resource://com.example.jboite/raw/sound1"),
            // Ajoutez d'autres sons ici au besoin
        )

        val buttonAddSound: Button = findViewById(R.id.buttonAddSound)
        buttonAddSound.setOnClickListener {
            showAddSoundDialog()
        }
    }

    private fun showAddSoundDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_sound, null)
        val editTextSoundName: EditText = dialogView.findViewById(R.id.editTextSoundName)
        val buttonChooseSound: Button = dialogView.findViewById(R.id.buttonChooseSound)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Sound")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val soundName = editTextSoundName.text.toString()
                if (soundName.isNotEmpty() && selectedSoundUri != null) {
                    sounds.add(Sound(soundName, selectedSoundUri.toString()))
                    // Mettez à jour l'adaptateur de la ListView ici si nécessaire
                    Toast.makeText(this, "Sound added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please select a sound and provide a name", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        buttonChooseSound.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_SOUND)
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_SOUND && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedSoundUri = uri
            }
        }
    }
}
