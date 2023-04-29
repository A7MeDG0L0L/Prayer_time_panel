package com.prayer_time_panel

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailManager
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailProvider

class Cocktail : SlookCocktailProvider() {

    override fun onUpdate(
        context: Context?,
        cocktailManager: SlookCocktailManager?,
        cocktailIds: IntArray?
    ) {
        println("onUpdate")
        super.onUpdate(context, cocktailManager, cocktailIds)
        if(context != null)  {
            val cm = cocktailManager ?: SlookCocktailManager.getInstance(context)
            val ids = cocktailIds ?: cm.getCocktailIds(ComponentName(context, Cocktail::class.java))
            println("onUpdate")
            if (ids != null) {
                val rv = RemoteViews(context.packageName, R.layout.cocktail_layout)
                val str = context.resources.getString(R.string.app_name)

                rv.setTextViewText(R.id.remote_list,str)
                setPendingIntent(context, rv)
                for (i in ids.indices) {
                    cocktailManager?.updateCocktail(ids[i], rv)
                }
            }
        }
    }

    private fun setPendingIntent(context: Context, rv: RemoteViews) {
        setPendingIntent(context, R.id.remote_list, Intent(Intent.ACTION_DIAL), rv)
        setPendingIntent(context, R.id.translate, Intent("android.media.action.IMAGE_CAPTURE"), rv)
        setPendingIntent(
            context,
            R.id.settings,
            Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")),
            rv
        )
    }

    private fun setPendingIntent(context: Context, rscId: Int, intent: Intent, rv: RemoteViews) {
        val itemClickPendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        rv.setOnClickPendingIntent(rscId, itemClickPendingIntent)
    }
}