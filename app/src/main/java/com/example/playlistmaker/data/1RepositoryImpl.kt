class IntentSettingsRepositoryImpl(val context:Context) :1Repository{
fun getIntentActionSend():Intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.practicum_android_link))
            intent.setType("text/plain")
        ??  if (intent.resolveActivity(context.packageManager) != null) {
                startActivity(Intent.createChooser(intent, context.getString(R.string.share_one)))
            } else {
                Toast.makeText(this, getString(R.string.share_no), Toast.LENGTH_SHORT).show()
            }
fun getIntent() {

 } 
}
