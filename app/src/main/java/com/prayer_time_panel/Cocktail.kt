package com.prayer_time_panel

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.RemoteViews
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailManager
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Cocktail : SlookCocktailProvider() {
    private var handler: Handler? = null
    override fun onUpdate(
        context: Context?, cocktailManager: SlookCocktailManager?, cocktailIds: IntArray?
    ) {
        println("onUpdate")
        super.onUpdate(context, cocktailManager, cocktailIds)
        if (context != null) {
            val cm = cocktailManager ?: SlookCocktailManager.getInstance(context)
            val ids = cocktailIds ?: cm.getCocktailIds(ComponentName(context, Cocktail::class.java))
            if (ids != null) {
             for (cocktailId in ids){
                 val rv = RemoteViews(context.packageName, R.layout.cocktail_layout)
                 val str = context.resources.getString(R.string.app_name)
                 val currentTimeMillis = System.currentTimeMillis()
                 val dateFormat = SimpleDateFormat("h:mm:ss a", Locale.getDefault())
                 val currentTimeString = dateFormat.format(Date(currentTimeMillis))
                 rv.setTextViewText(R.id.textView, currentTimeString)
                 cocktailManager?.updateCocktail(cocktailId, rv)

                 handler = Handler(Looper.getMainLooper())
                 handler?.post(object : Runnable {
                     override fun run() {
                         // Update the RemoteViews object with the current time
                         val currentTimeMillis = System.currentTimeMillis()
                         val dateFormat = SimpleDateFormat("h:mm:ss a", Locale.getDefault())
                         val currentTimeString = dateFormat.format(Date(currentTimeMillis))
                         rv.setTextViewText(R.id.textView, currentTimeString)

                         // Update the widget on the Edge panel using the SlookCocktailManager API
                         cocktailManager?.updateCocktail(cocktailId, rv)

                         // Schedule the next update in 1 second
                         handler?.postDelayed(this, 1000)
                     }
                 })


//                rv.setImageViewResource(R.id.imageView, R.drawable.avatar_)

                 // Create an array of hardcoded values
                 val values = arrayOf("Value 1", "Value 2", "Value 3")

                 val remoteIntent = Intent(context, Cocktail::class.java)
                 rv.setRemoteAdapter(R.id.remote_list, remoteIntent)
                 setPendingIntent(context, rv)
                 for (i in ids.indices) {
                     cocktailManager?.updateCocktail(ids[i], rv)
                 }
             }
            }
        }
    }

    private fun setPendingIntent(context: Context, rv: RemoteViews) {
//        setPendingIntent(context, R.id.remote_list, Intent(Intent.ACTION_DIAL), rv)
        setPendingIntent(context, R.id.textView, Intent(Intent.ACTION_DEFAULT), rv)
//        setPendingIntent(context, R.id.translate, Intent("android.media.action.IMAGE_CAPTURE"), rv)
        setPendingIntent(
            context,
            R.id.settings,
            Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")),
            rv
        )
    }

    private fun setPendingIntent(context: Context, rscId: Int, intent: Intent, rv: RemoteViews) {
        val itemClickPendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
        rv.setOnClickPendingIntent(rscId, itemClickPendingIntent)
    }

    override fun onVisibilityChanged(context: Context?, cocktailId: Int, visibility: Int) {
        super.onVisibilityChanged(context, cocktailId, visibility)
        println("On VisibilityChanged $visibility ")
        println("WWWWWWWWWWWWWWWWWWWWWWWW")
        if(visibility == 2) {
            println("Terminating Event Handler")
            handler?.removeCallbacksAndMessages(null)
        }else{
            println("Updating RemoteView")
            val cm = SlookCocktailManager.getInstance(context)
            val rv = RemoteViews(context?.packageName, R.layout.cocktail_layout)
            handler?.post(object : Runnable {
                override fun run() {
                    // Update the RemoteViews object with the current time
                    val currentTimeMillis = System.currentTimeMillis()
                    val dateFormat = SimpleDateFormat("h:mm:ss a", Locale.getDefault())
                    val currentTimeString = dateFormat.format(Date(currentTimeMillis))
                    rv.setTextViewText(R.id.textView, currentTimeString)

                    // Update the widget on the Edge panel using the SlookCocktailManager API
                    cm?.updateCocktail(cocktailId, rv)

                    // Schedule the next update in 1 second
                    handler?.postDelayed(this, 1000)
                }
            })
        }


    }
}