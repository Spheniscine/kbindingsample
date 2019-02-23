# KBinding Sample

A sample app demonstrating the KBinding library (pre-alpha development).

Currently has one screen, which demonstrates using KBinding together with Anko Layouts for an MVVM architecture. The screen itself allows the user select a country using a third-party [CountryCodePicker](https://github.com/joielechong/CountryCodePicker) (hence demostrating definition of custom bindings) and enter in a phone number. This can then be "translated" back and forth to the E164 international phone format.

KBinding is an alternative to the current [Data Binding Library](https://developer.android.com/topic/libraries/data-binding/) standard, but with several features that DBL doesn't or has limited support for:

* KBinding works with [Anko Layouts](https://github.com/Kotlin/anko/wiki/Anko-Layouts) out of the box, as XML is not required (just have your activity or fragment inherit from the KBindingClient interface\*). If you're not using Anko Layouts, you can still define bindings within the activity or fragment class, in onCreate (for activities) or onViewCreated (for fragments)
* KBindableVals and KBindableVars can be used as delegates, e.g. `var name by KBindableVar("John Smith")`. This allows you to get and set the property directly in the view model, e.g. `if(name != "") name = "Jane Newperson"` without tedious and error-prone boxing/un-boxing
* KBindables use LiveData as a base, but unlike LiveData has been made null-safe. There is a meaningful distinction between KBindableVar\<String> and KBindableVar<String?>, while LiveData-boxed values can always be set to null. Lateinit KBindableVars are also supported, but, just like native lateinit vars, will throw an exception if accessed before being set
* Custom two-way binding adapters (made by generating a KBindableVar instance from the view widget via KBindableVar.adapt) are automatically infinite-loop safe if bound using KBindingClient.bind2

\* I might soften or remove this requirement if that's something people want, perhaps leaving it for those that want to use the extension functions/properties
