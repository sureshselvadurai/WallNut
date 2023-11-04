import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.wallnut.R
import com.example.wallnut.model.OnboardingItem

/**
 * Adapter class for managing the onboarding ViewPager, responsible for inflating and managing
 * individual onboarding screens.
 *
 * @param context The application context.
 * @param onboardingItems A list of [OnboardingItem] objects representing the content of each
 * onboarding screen.
 */
class OnboardingPagerAdapter(
    private val context: Context,
    private val onboardingItems: List<OnboardingItem>
) : PagerAdapter() {

    /**
     * Inflates and adds an onboarding screen to the ViewPager.
     *
     * @param container The parent view that holds the onboarding screens.
     * @param position The position of the onboarding screen to be displayed.
     * @return The instantiated onboarding screen view.
     */
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.onboarding_activity_viewpage, null)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val titleTextView = view.findViewById<TextView>(R.id.textViewTitle)
        val descriptionTextView = view.findViewById<TextView>(R.id.descOnboard)

        val onboardingItem = onboardingItems[position]
        imageView.setImageResource(onboardingItem.imageResId)
        titleTextView.text = onboardingItem.title
        descriptionTextView.text = onboardingItem.description

        container.addView(view)
        return view
    }

    /**
     * Returns the total number of onboarding screens.
     *
     * @return The total number of onboarding screens.
     */
    override fun getCount(): Int {
        return onboardingItems.size
    }

    /**
     * Checks whether the view is associated with the object.
     *
     * @param view The view to be checked.
     * @param obj The object to be checked.
     * @return `true` if the view is associated with the object; otherwise, `false`.
     */
    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    /**
     * Removes an onboarding screen from the ViewPager.
     *
     * @param container The parent view that holds the onboarding screens.
     * @param position The position of the onboarding screen to be removed.
     * @param obj The onboarding screen view to be removed.
     */
    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}
