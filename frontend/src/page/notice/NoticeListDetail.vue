<template>
<div class="container col-sm-12 col-md-12 col-lg-12 p-0">
  <!-- background image -->
  <div class="notice-img" style="display:block;">
  </div>
<div class="container col-md-8">
    <!-- Title -->
    <div class="form-group mb-4 mt-5">
        <div class="d-flex justify-content-between" style="width:100%">
            <label class="d-flex notice-title-main mb-0 mr-3">{{notice.title}}</label>
                <div class="d-flex justify-content-end">
                    <small class="form-text notice-title-sub text-muted d-flex mr-2">작성일 : {{writeDate(this.notice.createDate)}}</small>
                    <small class="form-text notice-title-sub text-muted d-flex">|</small>
                    <small class="form-text notice-title-sub text-muted d-flex ml-2">조회수 : {{notice.visit}}</small>
                </div>
        </div>
    <hr>
    </div>

    <!-- Viewer -->
   <!-- <Viewer v-if="notice.content != null" :initialValue="notice.content"/> -->

    <!-- contents -->
    <div class="form-group">
        <label class="d-flex notice-content-main mr-3">Contents</label>
        <Viewer v-if="notice.content != null" :initialValue="notice.content" style="text-align:justify"/>
    </div>

    <!-- Button -->
    <div class="d-flex justify-content-end mb-5 mt-5">
      <button v-if="this.checkType == 'admin'" type="submit" class="btn btn-delete pr-0 mr-2" style="font-size: 1.1rem;" @click="noticemodify">
        <i class="far fa-edit mr-2"></i>수정하기
      </button>
      <button v-if="this.checkType == 'admin'" type="submit" class="btn btn-delete pr-0 mr-2" style="font-size: 1.1rem;" @click="noticedelete">
        <i class="fas fa-trash-alt mr-2"></i>삭제하기
      </button>
      <button type="submit" class="btn btn-outline pr-0" style="font-size: 1.1rem;" @click="goNotice">
        <i class="fas fa-th-list mr-2"></i>목록으로
      </button>
    </div>
  </div>
</div>
</template>

<script>
import axios from 'axios';
import '../../assets/css/noticelistdetail.css';

import "codemirror/lib/codemirror.css";
import "@toast-ui/editor/dist/toastui-editor.css";
import { Viewer } from "@toast-ui/vue-editor";

import Swal from 'sweetalert2';

const baseURL = process.env.VUE_APP_BACKURL;

export default {
    components: {
      Viewer
    },
    created(){
      if(this.$cookies.get("Auth-Token") != null) {
        this.authUser();
      }
      this.init();
    },
    data(){
        return{
            notice:[],
            superadmin:'admin@linkivity.com',
            email:'',
            checkType:"",
            // content: null,
        }
    },
    methods:{
      authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((response) => {
          this.email = response.data.email;
          this.checkType = response.data.checkType;
        })
        .catch((err) => {
          console.log(err);
        });
    },
        init() {
          axios
            .get(`${baseURL}/notice/detail/${this.$route.params.ID}`)
            .then((res) => {
                this.notice = res.data;
            })
            .catch((err) => {
              if(err.response.status == 400) {
                this.$router.push("/badRequest").catch(err => {
                });
              } else if(err.response.status == 500) {
                this.$router.push("/serverError").catch(err => {
                });
              }
            });
        },
        writeDate(createDate){
            var wd = this.notice.createDate+"";
            return wd.substring(0,10);
        },
        goNotice() {
            this.$router.push('/notice');
        },
        noticedelete(){
            Swal.fire({
                width: 350,
                text: '글을 삭제하시겠습니까?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#fff',
                cancelButtonColor: '#fff',
                confirmButtonText: '<a style="font-size:1rem; color:black">Delete</a>',
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
                    toast.addEventListener('mouseenter', Swal.stopTimer);
                    toast.addEventListener('mouseleave', Swal.resumeTimer);
                    },
                });
                Toast.fire({
                    icon: 'success',
                    title: '글이 삭제되었습니다.',
                });
                axios
                  .delete(`${baseURL}/notice/delete/${this.$route.params.ID}`)
                  .then(()=>{
                    this.$router.push(`/notice/`)
                  })
                  .catch((err)=>{
                    console.log(err)
                  });
                }
            });
        },
        noticemodify(){
            this.$router.push({
                name: 'NoticeUpdate',
                params: { ID: this.$route.params.ID }
            })
        },
    },
}
</script>

<style scoped>

</style>