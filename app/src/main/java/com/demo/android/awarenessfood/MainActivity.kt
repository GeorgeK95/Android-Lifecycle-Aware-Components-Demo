package com.demo.android.awarenessfood

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import com.demo.android.awarenessfood.data.Recipe
import com.demo.android.awarenessfood.databinding.ActivityMainBinding
import com.demo.android.awarenessfood.monitor.NetworkMonitor
import com.demo.android.awarenessfood.monitor.NetworkState
import com.demo.android.awarenessfood.monitor.UnavailableConnectionLifecycleOwner
import com.demo.android.awarenessfood.repositories.models.RecipeApiState
import com.demo.android.awarenessfood.viewmodels.MainViewModel
import com.demo.android.awarenessfood.viewmodels.UiLoadingState
import com.demo.android.awarenessfood.views.IngredientView
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main Screen
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var unavailableConnectionLifecycleOwner: UnavailableConnectionLifecycleOwner

  private lateinit var networkMonitor: NetworkMonitor
  private val networkObserver = NetworkObserver()

  private val viewModel: MainViewModel by viewModels()
  private lateinit var binding: ActivityMainBinding
  private var snackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    networkMonitor = NetworkMonitor(this, lifecycle)
    lifecycle.addObserver(networkMonitor)

    unavailableConnectionLifecycleOwner.addObserver(networkObserver)

    viewModel.recipeState.observe(this, Observer {
      when (it) {
        RecipeApiState.Error -> showNetworkUnavailableAlert(R.string.error)
        is RecipeApiState.Result -> buildViews(it.recipe)
      }
    })
    viewModel.getRandomRecipe()

    networkMonitor.networkAvailableStateFlow.asLiveData().observe(this, Observer { networkState ->
      handleNetworkState(networkState)
    })

    viewModel.loadingState.observe(this, Observer { uiLoadingState ->
      handleLoadingState(uiLoadingState)
    })
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.menu_refresh -> {
      clearViews()
      viewModel.getRandomRecipe()
      true
    }
    R.id.menu_trivia -> {
      startActivity(Intent(this, FoodTriviaActivity::class.java))
      true
    }
    else -> super.onOptionsItemSelected(item)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main_menu, menu)
    return true
  }

  private fun buildViews(recipe: Recipe) {
    with(binding) {
      recipeInstructionsTitle.text = getString(R.string.instructions)
      recipeIngredientsTitle.text = getString(R.string.ingredients)
      recipeName.text = recipe.title
      recipeSummary.text = HtmlCompat.fromHtml(recipe.summary, 0)
      recipeInstructions.text = HtmlCompat.fromHtml(recipe.instructions, 0)
      Picasso.get().load(recipe.image).into(recipeImage)
      recipe.ingredients.forEachIndexed { index, ingredient ->
        val ingredientView = IngredientView(this@MainActivity)
        ingredientView.setIngredient(ingredient)
        if (index != 0) {
          ingredientView.addDivider()
        }
        recipeIngredients.addView(ingredientView)
      }
    }
  }

  private fun clearViews() {
    with(binding) {
      recipeName.text = ""
      recipeSummary.text = ""
      recipeInstructions.text = ""
      recipeImage.setImageDrawable(null)
      recipeIngredientsTitle.text = ""
      recipeIngredients.removeAllViews()
      recipeInstructionsTitle.text = ""
    }
  }

  private fun showNetworkUnavailableAlert(message: Int) {
    snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.retry) {
          viewModel.getRandomRecipe()
        }.apply {
          view.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.purple_500))
          show()
        }
  }

  private fun handleLoadingState(uiLoadingState: UiLoadingState?) {
    when (uiLoadingState) {
      UiLoadingState.Loading -> {
        clearViews()
        binding.progressBar.isVisible = true
      }
      UiLoadingState.NotLoading -> binding.progressBar.isVisible = false
      else -> {}
    }
  }

  private fun handleNetworkState(networkState: NetworkState?) {
    when (networkState) {
      NetworkState.Unavailable -> unavailableConnectionLifecycleOwner.onConnectionLost()
      NetworkState.Available -> unavailableConnectionLifecycleOwner.onConnectionAvailable()
      else -> {}
    }
  }

  private fun removeNetworkUnavailableAlert() {
    snackbar?.dismiss()
  }

  inner class NetworkObserver : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
      onNetworkAvailable()
    }

    override fun onStop(owner: LifecycleOwner) {
      onNetworkUnavailable()
    }

    private fun onNetworkUnavailable() {
      showNetworkUnavailableAlert(R.string.network_is_unavailable)
    }

    private fun onNetworkAvailable() {
      removeNetworkUnavailableAlert()
    }
  }
}
