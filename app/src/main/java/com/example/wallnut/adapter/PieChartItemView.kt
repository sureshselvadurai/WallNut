package com.example.wallnut.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

/**
 * Custom view for displaying a pie chart.
 *
 * @property context The context in which the view is created.
 * @property attrs The attributes of the view.
 */
class PieChartItemView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var data = listOf(30f, 20f, 20f, 15f) // Values for the pie chart
    private var colors = listOf(Color.BLACK, Color.LTGRAY, Color.YELLOW, Color.GREEN)
    private var legends = listOf("Entertainment", "Food", "Utilities", "Loan")

    /**
     * Set the data and legends for the pie chart.
     *
     * @param data The data values for the pie chart.
     * @param colors The colors corresponding to the data segments.
     * @param legends The legends describing the data segments.
     */
    fun setData(data: List<Float>, colors: List<Int>, legends: List<String>) {
        this.data = data
        this.colors = colors
        this.legends = legends
        invalidate() // Request a redraw of the view
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width.coerceAtMost(height) / 3f

        if (data.isEmpty()) {
            // No data available, draw background color
            canvas.drawColor(Color.parseColor("#F8FDF8"))

            // Text format
            val textPaint = Paint()
            textPaint.color = Color.BLACK
            textPaint.textSize = 45f
            textPaint.textAlign = Paint.Align.CENTER

            // No data text
            val noDataText = "  No Spends for the month  :) "

            // Calculate text position
            val textBounds = Rect()
            textPaint.getTextBounds(noDataText, 0, noDataText.length, textBounds)
            val textX = centerX
            val textY = centerY + textBounds.height() / 2

            // Draw the text on the colored background
            canvas.drawText(noDataText, textX, textY, textPaint)
        } else {
            val rectF = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
            val paint = Paint()

            var startAngle = 0f
            for (i in data.indices) {
                val sweepAngle = 360 * (data[i] / data.sum())
                paint.color = colors[i]
                canvas.drawArc(rectF, startAngle, sweepAngle, true, paint)

                // Calculate the position to display the percentage text
                val angle = Math.toRadians(startAngle + sweepAngle / 2.0)
                val textX = centerX + (radius / 2) * cos(angle).toFloat()
                val textY = centerY + (radius / 2) * sin(angle).toFloat()

                // Display the percentage
                val percentage = String.format("%.1f%%", (data[i] / data.sum()) * 100)
                drawTextCentered(canvas, percentage, textX, textY)

                startAngle += sweepAngle
            }

            drawLegendList(canvas, legends, colors)
        }
    }

    private fun drawTextCentered(canvas: Canvas, text: String, x: Float, y: Float) {
        val textPaint = Paint()
        textPaint.color = Color.BLACK
        textPaint.textSize = 24f
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText(text, x, y, textPaint)
    }

    private fun drawLegendList(canvas: Canvas, legends: List<String>, colors: List<Int>) {
        val legendX = 50f
        var legendY = height - 100f
        val legendTextSize = 24f
        val legendBoxSize = 30f

        val textPaint = Paint()
        textPaint.color = Color.BLACK
        textPaint.textSize = legendTextSize

        for (i in legends.indices) {
            val colorBoxPaint = Paint()
            colorBoxPaint.color = colors[i]
            val colorBoxRect = RectF(legendX, legendY - legendBoxSize / 2, legendX + legendBoxSize, legendY + legendBoxSize / 2)
            canvas.drawRect(colorBoxRect, colorBoxPaint)

            val legendLabel = legends[i]
            canvas.drawText(legendLabel, legendX + legendBoxSize + 10f, legendY + legendTextSize / 2, textPaint)

            legendY -= legendTextSize * 2
        }
    }
}
