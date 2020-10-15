# TextSwitchTest
文本开关

```xml
<com.quanzi.tvswitch.TextSwitch
	android:id="@+id/textSwitch"
	android:layout_width="wrap_content"
	android:layout_height="wrap_content" 
/>
```
```xml
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
```
```xml
<declare-styleable name="TextSwitch">			
    <attr name="left_content" format="string" />      
    <attr name="left_color" format="color" />          
    <attr name="left_size" format="dimension" />      
    <attr name="right_content" format="string" />
    <attr name="right_color" format="color" />
    <attr name="right_size" format="dimension" />
    <attr name="bg" format="color" />                 
    <attr name="second_bg" format="color" />         
    <attr name="second_border_color" format="color" />    
    <attr name="second_border_width" format="dimension" />  
    <attr name="duration" format="integer" />              
</declare-styleable>   
```
