<template>
<div class="container col-sm-12 col-md-12 col-lg-12 p-0">
  <!-- background image -->
  <div class="notice-img" style="display:block;">
  </div>
  <div class="container col-md-8">
    <!-- Title -->
    <div class="form-group mb-4 mt-5">
        <div class="d-flex justify-content-start">
            <label class="d-flex notice-title-font mr-3">Title</label>
            <small class="form-text notice-content-font text-muted d-flex">제목을 수정해주세요.</small>
        </div>
      <input type="text" class="form-control" id="title" v-model="NoticeUpdate.title" />
    </div>
     
    <!-- Detail-Info -->
    <div class="form-group">
        <div class="d-flex justify-content-start">
            <label class="d-flex notice-title-font mr-3">Detail-Info</label>
            <small class="form-text notice-content-font text-muted d-flex">내용을 수정해주세요.</small>
      
        </div>
       
           <Editor ref="toastuiEditor" v-if="NoticeUpdate.content != null" :initialValue="NoticeUpdate.content"/>
            
      <!-- <textarea class="form-control" style="height:30rem;" id="detail" v-model="NoticeUpdate.content"></textarea> -->
    </div>

    <!-- Button -->
    <div class="d-flex justify-content-end mb-5">
      <button type="submit" class="btn btn-outline pr-0" style="font-size: 1.1rem;" @click="noticemodify">
        <i class="fas fa-pen mr-1"></i>수정
      </button>
    </div>
  </div>
</div>
</template>

<script>
import axios from 'axios';
import '../../assets/css/noticeupdate.css';
import Swal from "sweetalert2";
import "codemirror/lib/codemirror.css"; 
import "@toast-ui/editor/dist/toastui-editor.css"; 
import { Editor } from "@toast-ui/vue-editor";



const baseURL = process.env.VUE_APP_BACKURL;

export default {
     components:{
    Editor
  },
    data(){
        return{
            NoticeUpdate:[],
        }
    },
    created(){
        this.init();
    },
    methods:{
        init(){
            axios.get(`${baseURL}/notice/detail/${this.$route.params.ID}`)
            .then((res)=>{
                this.NoticeUpdate = res.data;
            }).catch((err)=>{
                console.log(err)
            })
        },
        noticemodify(){
            this.NoticeUpdate.content = this.$refs.toastuiEditor.invoke("getMarkdown");
            Swal.fire({
                width: 350,
                text: '수정하시겠습니까?',
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#fff',
                cancelButtonColor: '#fff',
                confirmButtonText: '<a style="font-size:1rem; color:black">Update</a>',
                cancelButtonText: '<a style="font-size:1rem; color:black">Cancel</a>',
            }).then((result) => {
                if (result.value) {
                const Toast = Swal.mixin({
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 1000,
                    timerProgressBar: true,
                    onOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer)
                    toast.addEventListener('mouseleave', Swal.resumeTimer)
                    }
                })
                Toast.fire({
                    icon: 'success',
                    title: 'Update Completed!'
                })
                axios.put(`${baseURL}/notice/modify`, this.NoticeUpdate)
                    .then(() => {
                    this.$router.push({
                        name: "NoticeListDetail",
                        params: { ID: this.$route.params.ID }
                    });
                    }).catch((error) => {
                    console.log(error)
                    })
                }
            })
        },
    },
    
}
</script>

<style>

</style>