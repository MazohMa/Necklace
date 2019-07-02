package necklace.viewmodelsupport

import necklace.NecklaceViewModel

interface ViewModelFactory {
    fun create(): NecklaceViewModel
}