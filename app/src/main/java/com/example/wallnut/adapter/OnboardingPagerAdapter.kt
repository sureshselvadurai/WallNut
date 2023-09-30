import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.wallnut.R
import com.example.wallnut.model.OnboardingItem

class OnboardingPagerAdapter(
    private val context: Context,
    private val onboardingItems: List<OnboardingItem>
) : PagerAdapter() {

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

    override fun getCount(): Int {
        return onboardingItems.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }



}
