function submitCheck(operate,value){
    subform=document.forms[0];
    thisid=subform.elements[0];
    if(value=='checkall'){  
        var j=0;
        if(subform.intMultibox.value!=null){
        subform.intMultibox.checked=subform.checkall.checked;
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
        for (var i=0;i<subform.intMultibox.length;i++)
                    {
                        subform.intMultibox[i].checked=subform.checkall.checked
                        if(subform.intMultibox[i].checked==true){
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
    if(value=='checkallsum'){  
        var j=0;
        if(subform.intMultibox.value!=null){
        subform.intMultibox.checked=subform.checkall.checked;
        if(subform.checkall.checked==true){
        	subform.sumbutton.disabled=false;
        	subform.sumendbutton.disabled=false;
        }
        else
        {
        	subform.sumbutton.disabled=true;
        	subform.sumendbutton.disabled=true;
        }
        return true;
        }
        for (var i=0;i<subform.intMultibox.length;i++)
                    {
                        subform.intMultibox[i].checked=subform.checkall.checked
                        if(subform.intMultibox[i].checked==true){
                            j++;
                        }
                    }
                    if((subform.checkall.checked==false) || (subform.checkall.checked==true &&  j==0)){
        						subform.sumbutton.disabled=true;
        						subform.sumendbutton.disabled=true;
                    }
                    if(subform.checkall.checked==true && j==1)
                    {
        						subform.sumbutton.disabled=false;
        						subform.sumendbutton.disabled=false;
                    }
                    if(subform.checkall.checked==true && j>1)
                    {
        						subform.sumbutton.disabled=false;
        						subform.sumendbutton.disabled=false;
                    }
                    return true;
    }   
     
    if(value=='search'){    
        subform.operate.value = value;
    return true;
    }  
    if(value=='listsearch'){    
        subform.operate.value = value;
    return true;
    }        
    if(value=='searchbytype'){    
        subform.operate.value = value;
    return true;
    } 
    if(value=='listsearchbytype'){    
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
    if(subform.intMultibox.value!=null){
        subform.intMultibox.checked=!subform.intMultibox.checked;
        if(subform.intMultibox.checked==true){
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
        for (var i=0;i<subform.intMultibox.length;i++)
                    {
                        
                        if(subform.intMultibox[i].value==operate){
                        subform.intMultibox[i].checked=!subform.intMultibox[i].checked
                                
                        }
                        if(subform.intMultibox[i].checked==true){
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
    if(value=='checkthissum'){     
    var j=0;
    if(subform.intMultibox.value!=null){
        subform.intMultibox.checked=!subform.intMultibox.checked;
        if(subform.intMultibox.checked==true){
        subform.sumbutton.disabled=false;
        subform.sumendbutton.disabled=false;
        }
        else
        {
        subform.sumbutton.disabled=true;
        subform.sumendbutton.disabled=true;
        }
        return true;
        }
        for (var i=0;i<subform.intMultibox.length;i++)
                    {
                        
                        if(subform.intMultibox[i].value==operate){
                        subform.intMultibox[i].checked=!subform.intMultibox[i].checked
                                
                        }
                        if(subform.intMultibox[i].checked==true){
                                j++;
                                }
                    }
                    if(j==0){
                    subform.sumbutton.disabled=true;
                    subform.sumendbutton.disabled=true;
                    }
                    
                    if(j==1){
                    subform.sumbutton.disabled=false;
                    subform.sumendbutton.disabled=false;
                    }
                    if(j>1){
                    subform.sumbutton.disabled=false;
                    subform.sumendbutton.disabled=false;
                    }
                    return true;
    }   
      
    if(value=='checkthisbox'){  
    var j=0;
    
    if(subform.intMultibox.value!=null){

        if(subform.intMultibox.checked){
        subform.editbutton.disabled=false;
        subform.delbutton.disabled=false;}
        else{
        subform.editbutton.disabled=true;
        subform.delbutton.disabled=true;
        }
        return true;
        }
        for (var i=0;i<subform.intMultibox.length;i++)
                    {
                        if(subform.intMultibox[i].checked==true){
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
    }else if(value=='checkthisboxsum'){  
    var j=0;
    
    if(subform.intMultibox.value!=null){

        if(subform.intMultibox.checked){
        subform.sumbutton.disabled=false;
        subform.sumendbutton.disabled=false;
        }else{
        subform.sumbutton.disabled=true;
        subform.sumendbutton.disabled=true;

        }
        return true;
        }
        for (var i=0;i<subform.intMultibox.length;i++)
                    {
                        if(subform.intMultibox[i].checked==true){
                                j++;
                                }
                    }
                    if(j==0){
                    subform.sumbutton.disabled=true;
        						subform.sumendbutton.disabled=true;

                    }
                    
                    if(j==1){
                    subform.sumbutton.disabled=false;
        						subform.sumendbutton.disabled=false;

                    }
                    if(j>1){
                    subform.sumbutton.disabled=false;
        						subform.sumendbutton.disabled=false;

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
    if(subform.intMultibox.checked==true){
                                thisid.value=subform.intMultibox.value;
                                return true;
                                }   
                           
        for (var i=0;i<subform.intMultibox.length;i++)
                    {
                        if(subform.intMultibox[i].checked==true){
                                thisid.value=subform.intMultibox[i].value;
                                bbbb=true;
                                }
                                if(bbbb){
                 
                                return true;
                                }
                    }
    
    }
    
    else if(value=='delete'){
    var j=0;
    if(subform.intMultibox.checked)
                    {
                        subform.operate.value = value;
                        if ( confirm('您确信要删除吗?'))
                        {
                            return true;
                        }
                        return false;
                    }
        for (var i=0;i<subform.intMultibox.length;i++)
                {
                    
                    if(subform.intMultibox[i].checked)
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