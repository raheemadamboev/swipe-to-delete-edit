package xyz.teamgravity.swipetodeleteedit

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeTouchAdapter(
    private val leftAction: SwipeTouchAction,
    private val rightAction: SwipeTouchAction,
    private val margin: Int
) : ItemTouchHelper.Callback() {

    var listener: SwipeTouchListener? = null

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int =
        makeMovementFlags(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT)

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        if (position != RecyclerView.NO_POSITION) {
            when (direction) {
                ItemTouchHelper.RIGHT -> listener?.onSwipeRight(position)
                ItemTouchHelper.LEFT -> listener?.onSwipeLeft(position)
                else -> Unit
            }
        }
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float = 0.05F

    override fun isItemViewSwipeEnabled(): Boolean = true

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (viewHolder.bindingAdapterPosition == RecyclerView.NO_POSITION) return

        val leftPosition = viewHolder.itemView.left - margin
        val rightPosition = viewHolder.itemView.right + margin
        val topPosition = viewHolder.itemView.top
        val bottomPosition = viewHolder.itemView.bottom
        val difference = bottomPosition - topPosition

        when {
            dX > 0 -> {
                leftAction.background.setBounds(leftPosition, topPosition, rightPosition, bottomPosition)
                leftAction.background.draw(c)

                val width = leftAction.icon.intrinsicWidth
                val height = leftAction.icon.intrinsicHeight

                val imageMargin = (difference - height) / 2
                val imageTop = topPosition + (difference - height) / 2
                val imageBottom = imageTop + height
                val imageLeft = leftPosition + imageMargin
                val imageRight = imageLeft + width

                leftAction.icon.setBounds(imageLeft, imageTop, imageRight, imageBottom)
                leftAction.icon.draw(c)
            }

            dX < -20F -> {
                rightAction.background.setBounds(leftPosition, topPosition, rightPosition, bottomPosition)
                rightAction.background.draw(c)

                val width = rightAction.icon.intrinsicWidth
                val height = rightAction.icon.intrinsicHeight

                val imageMargin = (difference - height) / 2
                val imageTop = topPosition + (difference - height) / 2
                val imageBottom = imageTop + height
                val imageRight = rightPosition - imageMargin
                val imageLeft = imageRight - width

                rightAction.icon.setBounds(imageLeft, imageTop, imageRight, imageBottom)
                rightAction.icon.draw(c)
            }

            else -> Unit
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun attachToRecyclerView(view: RecyclerView) {
        ItemTouchHelper(this).attachToRecyclerView(view)
    }

    ///////////////////////////////////////////////////////////////////////////
    // MISC
    ///////////////////////////////////////////////////////////////////////////

    data class SwipeTouchAction(
        val icon: Drawable,
        val background: ColorDrawable
    ) {
        companion object {
            fun fromResource(
                context: Context,
                @DrawableRes icon: Int,
                @ColorRes background: Int
            ): SwipeTouchAction {
                return SwipeTouchAction(
                    icon = ContextCompat.getDrawable(context, icon)!!,
                    background = ColorDrawable(ContextCompat.getColor(context, background))
                )
            }
        }
    }

    interface SwipeTouchListener {
        fun onSwipeRight(position: Int)
        fun onSwipeLeft(position: Int)
    }
}