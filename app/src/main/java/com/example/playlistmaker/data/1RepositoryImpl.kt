class IntentSettingsRepositoryImpl(val context:Context) :IntentSettingsRepository{
fun getIntentActionSend():Intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.practicum_android_link))
            intent.setType("text/plain")
        
fun getIntentSendTo() :Intent =
Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(context.getString(R.string.uri_mailto))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.my_mail_adress)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.my_mail_thema))
               putExtra(Intent.EXTRA_TEXT, context.getString(R.string.my_mail_message))
} 
fun getIntentActionView() :Intent =Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(context.getString(R.string.practicum_offer))
}
}
