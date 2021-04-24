<template>
<div>
  <div class="modal fade" id="findpassword" role="dialog" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content" style="width:80%; border:0;">
        <div class="modal-header pl-0" style="background-color:RGB(134, 165, 212); width:100%">
          <h4
            class="modal-title font-weight-bold my-auto ml-2"
            id="exampleModalLabel"
            style="width:60%;"
          ><i class="fas fa-search ml-5"><span class="ml-2" style="font-size:85%">비밀번호 찾기</span></i></h4>
          <button type="button" class="close my-auto ml-0" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body pb-0">
          <!-- email 입력칸 -->
          <div class="modal-body d-flex justify-content-center">
            <i class="fas fa-envelope my-auto mr-2" style="font-size:25px"></i>
            <!-- <i class="fas fa-envelope my-auto mr-2" style="font-size:25px; color:red" title="이메일 형식을 아닙니다!"></i> -->
            <input
              class="text-center"
              style="width:70%; border-radius:5px;"
              type="text"
              v-model="email"
              id="email"
              placeholder="example@email.com"
            />
          </div>
          <small class="d-flex justify-content-end mt-0 mb-0" style="width:86%; font-weight:bold">아이디를 입력해주세요<i class="fas fa-exclamation ml-1 my-auto"></i></small>

          <!-- 이름 입력칸 -->
          <div class="modal-body d-flex justify-content-center">
            <i class="fas fa-user-circle my-auto mr-2" style="font-size:25px"></i>
            <input
              class="text-center"
              style="width:70%; border-radius:5px;"
              type="text"
              v-model="name"
              id="password"
              placeholder="Input your Name"
            />
          </div>
          <small class="d-flex justify-content-end mt-0 mb-0" style="width:86%; font-weight:bold">이름을 입력해주세요<i class="fas fa-exclamation ml-1 my-auto"></i></small>
        </div>

        <div style="margin-left:10%; margin-right:10%;">
          <hr>
        </div>

        <!-- 비밀번호 찾기 -->
        <div @click="findpw" data-dismiss="modal" class="mt-2 row" style="cursor: pointer; margin-left:17%; margin-right:17%;">
          <div class="col-12 d-flex justify-content-center align-items-center" style="border:1px solid RGB(134, 165, 212); height:2rem; border-radius:5px">
            <i class="fas fa-unlock"><span class="my-auto ml-2">비밀번호 찾기</span></i>
          </div>
        </div>

        <div class="modal-footer border-0">
          <!-- <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary">Save changes</button> -->
        </div>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import axios from 'axios'
const baseURL = process.env.VUE_APP_BACKURL;
export default {
  data() {
    return {
      email: "",
      name: "",
    }
  },
  methods: {
    findpw() {
      if (!this.email && this.name) {
        const Toast = Swal.mixin({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1000,
          timerProgressBar: true,
          onOpen: (toast) => {
            toast.addEventListener("mouseenter", Swal.stopTimer);
            toast.addEventListener("mouseleave", Swal.resumeTimer);
          },
        });

        Toast.fire({
          icon: "error",
          title: "이메일을 적어주세요!",
        });
      } else if (this.email && !this.name) {
        const Toast = Swal.mixin({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1000,
          timerProgressBar: true,
          onOpen: (toast) => {
            toast.addEventListener("mouseenter", Swal.stopTimer);
            toast.addEventListener("mouseleave", Swal.resumeTimer);
          },
        });

        Toast.fire({
          icon: "error",
          title: "이름을 적어주세요!",
        });
      } else if (!this.email && !this.password) {
        const Toast = Swal.mixin({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1000,
          timerProgressBar: true,
          onOpen: (toast) => {
            toast.addEventListener("mouseenter", Swal.stopTimer);
            toast.addEventListener("mouseleave", Swal.resumeTimer);
          },
        });

        Toast.fire({
          icon: "error",
          title: "이메일 및 이름을 적어주세요!",
        });
      } else {
        axios.get(`${baseURL}/account/pwd/${this.email}/${this.name}`)
          .then((response) => {
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
              title: '임시 비밀번호를 전송중입니다.'
            })
            this.email = ""
            this.name = ""
          }).catch(() => {
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
              icon: 'error',
              title: '올바르지 않은 정보입니다.'
            })
          })
      }
    },
  },
}
</script>

<style>
.modal-open {
  overflow: hidden !important;
}
</style>