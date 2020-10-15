# TextSwitchTest
文本开关

<com.quanzi.tvswitch.TextSwitch
    android:id="@+id/textSwitch"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />

<com.quanzi.tvswitch.TextSwitch
    android:id="@+id/textSwitch2"
    android:layout_width="150dp"
    android:layout_height="50dp"
    android:layout_marginTop="50dp"
    app:bg="#ff0"
    app:duration="1000"
    app:left_color="#00f"
    app:left_content="最新"
    app:left_size="18sp"
    app:right_color="#00f"
    app:right_content="最热"
    app:right_size="26sp"
    app:second_bg="#0f0"
    app:second_border_color="#f0f" />
    
<declare-styleable name="TextSwitch">
    <attr name="left_content" format="string" />      //左边的文本   默认为左边
    <attr name="left_color" format="color" />         //左边文本的颜色  默认为#333333
    <attr name="left_size" format="dimension" />      //左边文本的大小，默认为14dp，如果左右大小只填一个，另外一个默认为当前大小
    <attr name="right_content" format="string" />
    <attr name="right_color" format="color" />
    <attr name="right_size" format="dimension" />
    <attr name="bg" format="color" />                 //背景色
    <attr name="second_bg" format="color" />          //选择色
    <attr name="second_border_color" format="color" />    //边框颜色
    <attr name="second_border_width" format="dimension" />  //边框宽度
    <attr name="duration" format="integer" />               //开关动画时长
</declare-styleable>    
