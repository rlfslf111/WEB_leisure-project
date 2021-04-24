<template>
  <div class="input-group mb-3">
    <textarea
      type="text"
      class="form-control"
      aria-label="Recipient's username"
      aria-describedby="button-addon2"
      style="height:2.5rem"
      v-model="newContent"
    />
    <div class="input-group-append">
      <button class="btn btn-default" style="border-radius:5px; border:1px solid gray; border-top-left-radius: 0;
    border-bottom-left-radius: 0; font-size:12px;" type="button" id="button-addon2" @click="replyModify">수정</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
const baseURL = process.env.VUE_APP_BACKURL;
export default {
  data() {
    return {
      newContent:"",
      replyUpdateData:[],
      tempContent:"",
    }
  },
    props: {
        reply: Object,
    },
    methods: {
        replyModify() {
            this.replyUpdateData = this.reply;
            this.replyUpdateData.content = this.newContent;
            Swal.fire({
              width: 350,
              text: "답글을 수정하시겠습니까?",
              icon: 'warning',
              showCancelButton: true,
              confirmButtonColor: '#fff',
              cancelButtonColor: '#fff',
              confirmButtonText: '<a style="font-size:1rem; color:black">Update</a>',
              cancelButtonText: '<a style="font-size:1rem; color:black">Cancel</a>'
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
                  if (this.replyUpdateData.content == "") {
                    Swal.fire({
                      width: 250,
                      icon: "warning",
                      text: "내용을 입력해주세요!",
                      confirmButtonText:
                        '<a style="font-size:1rem; color:black; width:0.5rem">확인</a>',
                    });
                  } else {
                    axios.put(`${baseURL}/reply/reModify`,this.replyUpdateData)
                      .then(() => {
                        Toast.fire({
                          icon: 'success',
                          title: '답글 수정 완료!'
                        })
                        setTimeout(() => {
                          this.$router.go()
                        },1000)
                      }).catch((error) => {
                        console.log(error.response.data)
                      })
                  }
                }
              })
        },
    },
    created() {
      this.tempContent = this.reply.content;
      this.newContent = this.tempContent;
    },
}
</script>

<style>

</style>