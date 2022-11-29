package com.demo.android.awarenessfood.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textview.MaterialTextView
import com.demo.android.awarenessfood.R
import com.demo.android.awarenessfood.data.Ingredient
import com.squareup.picasso.Picasso

class IngredientView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

  init {
    View.inflate(context, R.layout.ingredient_item, this)
  }

  fun setIngredient(ingredient: Ingredient) {
    val ingredientName = findViewById<MaterialTextView>(R.id.ingredient_name)
    val ingredientImage = findViewById<AppCompatImageView>(R.id.ingredient_image)

    ingredientName.text = context.getString(R.string.ingredient, ingredient.amount, ingredient.unit, ingredient.name)
    Picasso.get().load(ingredient.imageUrl).into(ingredientImage)
  }

  fun addDivider() {
    val divider = View(context)
    val dividerHeight = (resources.displayMetrics.density * 1).toInt()

    divider.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight)
    divider.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDivider))
    addView(divider)
  }
}