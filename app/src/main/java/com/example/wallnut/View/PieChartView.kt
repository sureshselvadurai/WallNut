package com.example.wallnut.View

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class PieChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val data = listOf(30f, 25f, 20f, 15f) // Values for the pie chart
    private val colors = listOf(Color.RED, Color.LTGRAY, Color.YELLOW, Color.BLUE)
    private val legends = listOf("Entertainment", "Food", "Utilities", "Loan")

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = Math.min(width, height) / 3f

        val rectF = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        val paint = Paint()

        var startAngle = 0f
        for (i in 0 until data.size) {
            val sweepAngle = 360 * (data[i] / data.sum())
            paint.color = colors[i]
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint)

            // Calculate the position to display the percentage text
            val angle = Math.toRadians(startAngle + sweepAngle / 2.0)
            val textX = centerX + (radius / 2) * Math.cos(angle).toFloat()
            val textY = centerY + (radius / 2) * Math.sin(angle).toFloat()

            // Display the percentage
            val percentage = String.format("%.1f%%", (data[i] / data.sum()) * 100)
            drawTextCentered(canvas, percentage, textX, textY)

            startAngle += sweepAngle
        }

        drawLegendList(canvas, legends, colors)
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
