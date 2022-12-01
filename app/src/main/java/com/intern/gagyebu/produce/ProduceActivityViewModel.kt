package com.intern.gagyebu.produce

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.*
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.room.data.UpdateDate
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/** addActivity-ViewModel **/

class ProduceActivityViewModel : ViewModel() {

    //날짜 입력 livedata
    private var _date: MutableLiveData<String> = MutableLiveData()
    val date: LiveData<String> get() = _date

    //항목 제목 livedata
    private var _title: MutableLiveData<String> = MutableLiveData()
    val title: LiveData<String> get() = _title

    //항목 금액 livedata
    private var _amount: MutableLiveData<String> = MutableLiveData()
    val amount: LiveData<String> get() = _amount

    //항목 카테고리 livedata
    private var _category: MutableLiveData<String> = MutableLiveData("수입")
    val category: LiveData<String> get() = _category

    var activityTitle: String = "항목 저장"

    var itemId: Int = 0

    private var isSaving: MutableLiveData<Boolean> = MutableLiveData(false)

    fun updateTitle(title: String) {
        _title.value = title
        //_title.postValue()
    }

    fun updateAmount(amount: String) {
        _amount.value = amount
    }

    fun updateCategory(category: String) {
        _category.value = category
    }

    fun updateDate(date: String) {
        _date.value = date
    }

    private val _eventFlow = MutableSharedFlow<Event>()

    val eventFlow = _eventFlow.asSharedFlow()

    /**
     * @param title 항목 제목
     * @param amount 금액
     * @param date 날찌
     * @param isSave 저장중인지 확인 (Boolean)
     * @return Boolean (asLiveData)
     *
     * 각 항목의 flow를 받음. 항목에 대한 입력이 없거나, 저장중이면 저장버튼 비활성화
     */
    var areInputsValid = combine(
        title.asFlow(),
        amount.asFlow(),
        date.asFlow(),
        isSaving.asFlow()
    ) { title, amount, date, isSaving -> title.isNotBlank() && amount.isNotBlank() && date.isNotBlank() && isSaving == false }.onStart {
        emit(
            false
        )
    }.asLiveData()


    //저장, 수정버튼 클릭시 함수 실행
    fun setData() {
        //각 입력값에 대한 validation 진행. 적절한 값이 아닌경우 Event 방출
        try {
            val dateArr =
                date.value?.split("-")?.map { it.toInt() }?.toIntArray()
                    ?: throw java.lang.IllegalArgumentException("날짜을 확인해주세요")

            val title = title.value?.trim()
                ?: throw java.lang.IllegalArgumentException("제목을 확인해주세요")

            val amount = amount.value?.let {
                if (it[0] == '0' || it.length > 8) {
                    throw java.lang.IllegalArgumentException("금액을 확인해주세요")
                } else {
                    Integer.parseInt(it)
                }

            } ?: throw java.lang.IllegalArgumentException("금액을 확인해주세요")
            val category =
                category.value ?: throw java.lang.IllegalArgumentException("카테고리를 확인해주세요")

            //값이 적절하고 저장해야 하는 경우.
            if (activityTitle == "항목 저장") {
                val itemEntity = ItemEntity(
                    id = 0,
                    amount = amount,
                    title = title,
                    year = dateArr[0],
                    month = dateArr[1],
                    day = dateArr[2],
                    category = category
                )
                setData(itemEntity)

            //값이 적절하고 수정해야 하는 경우.
            } else {
                val itemEntity = ItemEntity(
                    id = itemId,
                    amount = amount,
                    title = title,
                    year = dateArr[0],
                    month = dateArr[1],
                    day = dateArr[2],
                    category = category
                )
                updateData(itemEntity)
            }

        } catch (e: IllegalArgumentException) {
            e.message?.let { event(Event.Error(it)) } ?: event(Event.Error("오류가 발생하였습니다."))
        }
    }

    //항목 저장 함수
    private fun setData(itemEntity: ItemEntity) {
        //저장이 진행되는동안 저장버튼 비활성화
        isSaving.value = true

        CoroutineScope(Dispatchers.IO).launch {
            try {
                /**
                 * 5초 이내 저장이 완료되는 경우 Event.Done 방출 -> activity 종료
                 * 알수없는 오류 또는 저장에 실패하는 경우 -> Event.Error 방출 -> 재시도
                 */
                withTimeout(5000) {
                    launch {
                        //delay(4000)
                        ItemRepo.saveItem(itemEntity)
                        event(Event.Done("저장 완료"))
                    }
                }
            } catch (e: TimeoutCancellationException) {
                isSaving.value = false
                event(Event.Error("저장 실패"))
            }
        }
    }

    private fun updateData(itemEntity: ItemEntity) {
        isSaving.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                /**
                 * 5초 이내 저장이 완료되는 경우 Event.Done 방출 -> activity 종료
                 * 알수없는 오류 또는 저장에 실패하는 경우 -> Event.Error 방출 -> 재시도
                 */
                withTimeout(2000) {
                    launch {
                        ItemRepo.updateItem(itemEntity)
                        event(Event.Done("수정 완료"))
                    }
                }
            } catch (e: TimeoutCancellationException) {
                isSaving.value = false
                event(Event.Error("수정 실패"))
            }
        }
    }

    //이밴트를 받아 방출하는 함수
    private fun event(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    /**
     * 수정해야 하는 경우 Parcel 받아 항목 초기화
     *  date 날짜
     *  title 제목
     *  amount 금액
     *  category 카테고리
     *  activityTitle Activity 제목
     *  itemId 업데이트에 필요한 id
     */
    fun initUpdate(bundle: Bundle) {
        bundle.parcelable<UpdateDate>("updateData")?.let {
            _date.value = it.date
            _title.value = it.title
            _amount.value = it.amount.toString()
            _category.value = it.category
            activityTitle = "항목 수정"
            itemId = it.id
        }
    }

    sealed class Event {
        data class Error(val value: String) : Event()
        data class Done(val value: String) : Event()
    }

    //getParcelable 사용가능 API>=33. 하위버전에 getParcelable 을 위한 inline function
    inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }
}