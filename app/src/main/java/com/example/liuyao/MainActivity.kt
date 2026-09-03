package com.example.liuyao

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.liuyao.databinding.ActivityMainBinding
import com.example.liuyao.util.LunarCalendar
import com.example.liuyao.util.XiaoLiuRenCalculator
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSpinners()
        setupListeners()
        updateLunarInfo()
    }

    private fun initSpinners() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        // 年份（1900-2100）
        val years = (1900..2100).map { it.toString() }
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerYear.adapter = yearAdapter
        binding.spinnerYear.setSelection(years.indexOf(currentYear.toString()))

        // 月份（1-12）
        val months = (1..12).map { it.toString() }
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMonth.adapter = monthAdapter
        binding.spinnerMonth.setSelection(Calendar.getInstance().get(Calendar.MONTH))

        // 日期（1-31）
        val days = (1..31).map { it.toString() }
        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDay.adapter = dayAdapter
        binding.spinnerDay.setSelection(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1)

        // 小时（0-23）
        val hours = (0..23).map { it.toString().padStart(2, '0') }
        val hourAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hours)
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerHour.adapter = hourAdapter
        binding.spinnerHour.setSelection(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))

        // 分钟（0-59，每5分钟）
        val minutes = (0..59 step 5).map { it.toString().padStart(2, '0') }
        val minuteAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, minutes)
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMinute.adapter = minuteAdapter
        val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)
        binding.spinnerMinute.setSelection(currentMinute / 5)
    }

    private fun setupListeners() {
        // 取当前时间
        binding.btnNowTime.setOnClickListener {
            val now = Calendar.getInstance()
            binding.spinnerYear.setSelection(now.get(Calendar.YEAR) - 1900)
            binding.spinnerMonth.setSelection(now.get(Calendar.MONTH))
            binding.spinnerDay.setSelection(now.get(Calendar.DAY_OF_MONTH) - 1)
            binding.spinnerHour.setSelection(now.get(Calendar.HOUR_OF_DAY))
            binding.spinnerMinute.setSelection(now.get(Calendar.MINUTE) / 5)
            updateLunarInfo()
        }

        // 清空重算
        binding.btnClear.setOnClickListener {
            binding.tvLunarYear.text = ""
            binding.tvLunarMonth.text = ""
            binding.tvLunarDay.text = ""
            binding.tvShiChen.text = ""
            binding.tvShengXiao.text = ""
            binding.layoutResult.visibility = View.GONE
            binding.tvSyncTip.text = getString(R.string.sync_tip)
        }

        // 开始推算
        binding.btnCalculate.setOnClickListener {
            calculateResult()
        }

        // 监听公历变化，自动更新农历
        binding.spinnerYear.setOnItemSelectedListener(createDateChangeListener())
        binding.spinnerMonth.setOnItemSelectedListener(createDateChangeListener())
        binding.spinnerDay.setOnItemSelectedListener(createDateChangeListener())
        binding.spinnerHour.setOnItemSelectedListener(createDateChangeListener())
        binding.spinnerMinute.setOnItemSelectedListener(createDateChangeListener())
    }

    private fun createDateChangeListener() = object : android.widget.AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
            updateLunarInfo()
        }

        override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
    }

    private fun updateLunarInfo() {
        try {
            val year = binding.spinnerYear.selectedItem.toString().toInt()
            val month = binding.spinnerMonth.selectedItem.toString().toInt()
            val day = binding.spinnerDay.selectedItem.toString().toInt()
            val hour = binding.spinnerHour.selectedItem.toString().toInt()

            // 阳历转农历
            val lunarDate = LunarCalendar.solarToLunar(year, month, day)

            // 显示农历信息
            binding.tvLunarYear.text = lunarDate.getGanZhiYear()
            binding.tvLunarMonth.text = lunarDate.getMonthName()
            binding.tvLunarDay.text = lunarDate.getDayName()
            binding.tvShiChen.text = LunarCalendar.getShiChenName(hour)
            binding.tvShengXiao.text = lunarDate.getShengXiao()

            binding.tvSyncTip.text = getString(R.string.sync_success)
        } catch (e: Exception) {
            binding.tvSyncTip.text = "公历 ↔ 农历 转换失败"
        }
    }

    private fun calculateResult() {
        try {
            val year = binding.spinnerYear.selectedItem.toString().toInt()
            val month = binding.spinnerMonth.selectedItem.toString().toInt()
            val day = binding.spinnerDay.selectedItem.toString().toInt()
            val hour = binding.spinnerHour.selectedItem.toString().toInt()
            val minute = binding.spinnerMinute.selectedItem.toString().toInt()

            // 阳历转农历
            val lunarDate = LunarCalendar.solarToLunar(year, month, day)
            val shiChen = LunarCalendar.getShiChen(hour)

            // 计算小六壬
            val result = XiaoLiuRenCalculator.calculate(lunarDate.month, lunarDate.day, shiChen)

            // 显示结果
            binding.layoutResult.visibility = View.VISIBLE

            // 时间信息
            val timeInfo = buildString {
                append("公历：${year}年${month}月${day}日 ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}\n")
                append("农历：${lunarDate.getGanZhiYear()} ${lunarDate.getMonthName()}${lunarDate.getDayName()} ${LunarCalendar.getShiChenName(hour)}")
            }
            binding.tvResultTime.text = timeInfo

            // 三宫
            binding.tvMoonPalace.text = result.getMonthName()
            binding.tvMoonPalace.setTextColor(getColor(if (XiaoLiuRenCalculator.isJi(result.monthIndex)) R.color.ji_color else R.color.xiong_color))

            binding.tvDayPalace.text = result.getDayName()
            binding.tvDayPalace.setTextColor(getColor(if (XiaoLiuRenCalculator.isJi(result.dayIndex)) R.color.ji_color else R.color.xiong_color))

            binding.tvHourPalace.text = result.getHourName()
            binding.tvHourPalace.setTextColor(getColor(if (XiaoLiuRenCalculator.isJi(result.hourIndex)) R.color.ji_color else R.color.xiong_color))

            // 断语
            val duanYu = buildString {
                append("【${result.getMonthName()}】${result.getMonthDuanYu()}\n\n")
                append("【${result.getDayName()}】${result.getDayDuanYu()}\n\n")
                append("【${result.getHourName()}】${result.getHourDuanYu()}")
            }
            binding.tvDuanYu.text = duanYu

            // 滚动到结果
            binding.root.post {
                binding.root.smoothScrollTo(0, binding.layoutResult.top)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "计算失败：${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
