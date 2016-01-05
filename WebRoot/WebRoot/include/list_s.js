function submitCheck(operate,value){
    subform=document.forms[0];
    thisid=subform.elements[0];
    if(value=='checkall'){  
        var j=0;
        if(subform.stringMultibox.value!=null){
        subform.stringMultibox.checked=subform.checkall.checked;
        if(subform.checkall.checked==true){
        subform.editbutton.disabled=false;
        subform.delbutton.disabled=false;
        }
        else
        {
        subform.editbutton.disabled=true;
        subform.delbutton.disabled=true;
        }
        return true;
        }
        for (var i=0;i<subform.stringMultibox.length;i++)
                    {
                        subform.stringMultibox[i].checked=subform.checkall.checked
                        if(subform.stringMultibox[i].checked==true){
                            j++;
                        }
                    }
                    if((subform.checkall.checked==false) || (subform.checkall.checked==true &&  j==0)){
                    subform.editbutton.disabled=true;
                    subform.delbutton.disabled=true;
                    }
                    if(subform.checkall.checked==true && j==1)
                    {
                    subform.editbutton.disabled=false;
                    
                    subform.delbutton.disabled=false;
                    }
                    if(subform.checkall.checked==true && j>1)
                    {
                    subform.editbutton.disabled=true;
                    
                    subform.delbutton.disabled=false;
                    }
                    return true;
    }   
    if(value=='search'){    
        subform.operate.value = value;
    return true;
    }   
    if(value=='dbclicksubmit'){     
        subform.operate.value='ready_edit';
        thisid.value=operate;
        subform.submit();
        
    }   
    if(value=='checkthis'){     
    var j=0;
    if(subform.stringMultibox.value!=null){
        subform.stringMultibox.checked=!subform.stringMultibox.checked;
        if(subform.stringMultibox.checked==true){
        subform.editbutton.disabled=false;
        subform.delbutton.disabled=false;
        }
        else
        {
        subform.editbutton.disabled=true;
        subform.delbutton.disabled=true;
        }
        return true;
        }
        for (var i=0;i<subform.stringMultibox.length;i++)
                    {
                        
                        if(subform.stringMultibox[i].value==operate){
                        subform.stringMultibox[i].checked=!subform.stringMultibox[i].checked
                                
                        }
                        if(subform.stringMultibox[i].checked==true){
                                j++;
                                }
                    }
                    if(j==0){
                    subform.delbutton.disabled=true;
                    subform.editbutton.disabled=true;
                    }
                    
                    if(j==1){
                    subform.editbutton.disabled=false;
                    subform.delbutton.disabled=false;
                    }
                    if(j>1){subform.editbutton.disabled=true;
                    subform.delbutton.disabled=false;
                    }
                    return true;
    }   
    if(value=='checkthisbox'){  
    var j=0;
    
    if(subform.stringMultibox.value!=null){

        if(subform.stringMultibox.checked){
        subform.editbutton.disabled=false;
        subform.delbutton.disabled=false;}
        else{
        subform.editbutton.disabled=true;
        subform.delbutton.disabled=true;
        }
        return true;
        }
        for (var i=0;i<subform.stringMultibox.length;i++)
                    {
                        if(subform.stringMultibox[i].checked==true){
                                j++;
                                }
                    }
                    if(j==0){
                    subform.delbutton.disabled=true;
                    subform.editbutton.disabled=true;
                    }
                    
                    if(j==1){
                    subform.editbutton.disabled=false;
                    subform.delbutton.disabled=false;
                    }
                    if(j>1){subform.editbutton.disabled=true;
                    subform.delbutton.disabled=false;
                    }
                    return true;
    }   
    else if(value=='ready_add'){    
    subform.operate.value = value;
    return true;
    }
    else if(value=='ready_edit'){   
    subform.operate.value = value;
    var bbbb=false;
    if(subform.stringMultibox.checked==true){
                                thisid.value=subform.stringMultibox.value;
                                return true;
                                }   
                           
        for (var i=0;i<subform.stringMultibox.length;i++)
                    {
                        if(subform.stringMultibox[i].checked==true){
                                thisid.value=subform.stringMultibox[i].value;
                                bbbb=true;
                                }
                                if(bbbb){
                 
                                return true;
                                }
                    }
    
    }
    
    else if(value=='delete'){
    var j=0;
    if(subform.stringMultibox.checked)
                    {
                        subform.operate.value = value;
                        if ( confirm('您确信要删除吗?'))
                        {
                            return true;
                        }
                        return false;
                    }
        for (var i=0;i<subform.stringMultibox.length;i++)
                {
                    
                    if(subform.stringMultibox[i].checked)
                    {
                       subform.operate.value = value;
                        if ( confirm('您确信要删除吗?'))
                        {
                            return true;
                        }
                        return false;
                    }
                   
                }
                alert('您未选中任何主题！');
    
    }
    else{return false;}
}

function selectPage(next)
{       
subform=document.forms[0];
            subform.jumpState.value=next;
            subform.submit();                   
}

function submitCheck1(operate,value){
    subform=document.forms[0];
    thisid=subform.elements[0];
    if(value=='checkall'){  
        var j=0;
        if(subform.stringMultibox.value!=null){
        subform.stringMultibox.checked=subform.checkall.checked;
        if(subform.checkall.checked==true){
        subform.editbutton.disabled=false;
        subform.delbutton.disabled=false;
        }
        else
        {
        //subform.editbutton.disabled=true;
        subform.delbutton.disabled=true;
        }
        return true;
        }
        for (var i=0;i<subform.stringMultibox.length;i++)
                    {
                        subform.stringMultibox[i].checked=subform.checkall.checked
                        if(subform.stringMultibox[i].checked==true){
                            j++;
                        }
                    }
                    if((subform.checkall.checked==false) || (subform.checkall.checked==true &&  j==0)){
                    //subform.editbutton.disabled=true;
                    subform.delbutton.disabled=true;
                    }
                    if(subform.checkall.checked==true && j==1)
                    {
                    subform.editbutton.disabled=false;
                    
                    subform.delbutton.disabled=false;
                    }
                    if(subform.checkall.checked==true && j>1)
                    {
                    //subform.editbutton.disabled=true;
                    
                    subform.delbutton.disabled=false;
                    }
                    return true;
    }   
    if(value=='search'){    
        subform.operate.value = value;
    return true;
    }   
    if(value=='dbclicksubmit'){     
        subform.operate.value='ready_edit';
        thisid.value=operate;
        subform.submit();
        
    }   
    if(value=='checkthis'){     
    var j=0;
    if(subform.stringMultibox.value!=null){
        subform.stringMultibox.checked=!subform.stringMultibox.checked;
        if(subform.stringMultibox.checked==true){
        //subform.editbutton.disabled=false;
        subform.delbutton.disabled=false;
        }
        else
        {
        //subform.editbutton.disabled=true;
        subform.delbutton.disabled=true;
        }
        return true;
        }
        for (var i=0;i<subform.stringMultibox.length;i++)
                    {
                        
                        if(subform.stringMultibox[i].value==operate){
                        subform.stringMultibox[i].checked=!subform.stringMultibox[i].checked
                                
                        }
                        if(subform.stringMultibox[i].checked==true){
                                j++;
                                }
                    }
                    if(j==0){
                    subform.delbutton.disabled=true;
                    //subform.editbutton.disabled=true;
                    }
                    
                    if(j==1){
                    //subform.editbutton.disabled=false;
                    subform.delbutton.disabled=false;
                    }
                    if(j>1){//subform.editbutton.disabled=true;
                    subform.delbutton.disabled=false;
                    }
                    return true;
    }   
    if(value=='checkthisbox'){  
    var j=0;
    
    if(subform.stringMultibox.value!=null){

        if(subform.stringMultibox.checked){
        //subform.editbutton.disabled=false;
        subform.delbutton.disabled=false;}
        else{
        //subform.editbutton.disabled=true;
        subform.delbutton.disabled=true;
        }
        return true;
        }
        for (var i=0;i<subform.stringMultibox.length;i++)
                    {
                        if(subform.stringMultibox[i].checked==true){
                                j++;
                                }
                    }
                    if(j==0){
                    subform.delbutton.disabled=true;
                    //subform.editbutton.disabled=true;
                    }
                    
                    if(j==1){
                    //subform.editbutton.disabled=false;
                    subform.delbutton.disabled=false;
                    }
                    if(j>1){//subform.editbutton.disabled=true;
                    subform.delbutton.disabled=false;
                    }
                    return true;
    }   
    else if(value=='ready_add'){    
    subform.operate.value = value;
    return true;
    }
    else if(value=='ready_edit'){   
    subform.operate.value = value;
    var bbbb=false;
    if(subform.stringMultibox.checked==true){
                                thisid.value=subform.stringMultibox.value;
                                return true;
                                }   
                           
        for (var i=0;i<subform.stringMultibox.length;i++)
                    {
                        if(subform.stringMultibox[i].checked==true){
                                thisid.value=subform.stringMultibox[i].value;
                                bbbb=true;
                                }
                                if(bbbb){
                 
                                return true;
                                }
                    }
    
    }
    
    else if(value=='delete'){
    var j=0;
    if(subform.stringMultibox.checked)
                    {
                        subform.operate.value = value;
                        if ( confirm('您确信要删除吗?'))
                        {
                            return true;
                        }
                        return false;
                    }
        for (var i=0;i<subform.stringMultibox.length;i++)
                {
                    
                    if(subform.stringMultibox[i].checked)
                    {
                       subform.operate.value = value;
                        if ( confirm('您确信要删除吗?'))
                        {
                            return true;
                        }
                        return false;
                    }
                   
                }
                alert('您未选中任何主题！');
    
    }
    else{return false;}
}
