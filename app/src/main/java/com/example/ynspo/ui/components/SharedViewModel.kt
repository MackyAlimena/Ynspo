import androidx.lifecycle.ViewModel
import com.example.ynspo.data.model.UnsplashPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    var selectedPhoto: UnsplashPhoto? = null
}
