<template>
  <div class="modal fade" id="blacklist" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content container">
        <div class="modal-header border-0 pl-3 pb-0">
          <div class="modal-title w-100 text-left font-weight-bold" id="exampleModalLabel">
            <span style="color:crimson;">{{ blacklist.remail }} </span>
            <span>님의 신고내용</span>
          </div>
          <button type="button" class="close pl-0" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body pt-0">
          <hr />
          <!-- 신고자 -->
          <div class="row mb-2">
            <div class="col-md-5 text-left">
              <span style="font-size:1.2rem; font-weight:bold;">신고자</span>
            </div>
            <div class="col-md-7 text-left pl-3">
              <span style="font-weight:bold">{{ blacklist.email }}</span>
            </div>
          </div>

          <!-- 신고수 -->
          <div class="row mb-2">
            <div class="col-md-5 text-left">
              <span style="font-size:1.2rem; font-weight:bold;">신고당한 횟수</span>
            </div>
            <div class="col-md-7 text-left pl-3">
              <span style="font-weight:bold">{{ blacklist.cnt }}</span>
            </div>
          </div>

          <!-- 신고내용 -->
          <div class="row mb-2">
            <div class="col-md-5 text-left">
              <span style="font-size:1.2rem; font-weight:bold;">신고내용</span>
            </div>
            <div class="col-md-7"></div>
          </div>
          <div>
            <div class="form-group">
              <textarea class="form-control" rows="5" readonly v-model="blacklist.reason"> </textarea>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-sm"
            style="background-color:rgb(134, 165, 212);color:white;font-weight:bold; "
            data-dismiss="modal"
            @click="cancelblack"
          >
            거절
          </button>
          <button type="button" class="btn btn-sm" style="background-color:darkorange;color:white;font-weight:bold;" data-dismiss="modal" @click="warnblack">
            경고
          </button>
          <button type="button" class="btn btn-sm btn-danger" data-dismiss="modal" @click="dropblack" style="font-weight:bold;">
            탈퇴
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    blacklist: Object,
  },
  methods: {
    warnblack() {
      Swal.fire({
        width: 350,
        text: '회원을 경고처리 하시겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#fff',
        cancelButtonColor: '#fff',
        confirmButtonText: '<a style="font-size:1rem; color:black;">경고</a>',
        cancelButtonText: '<a style="font-size:1rem; color:black;">취소</a>',
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
            title: '회원을 경고처리 했습니다.',
          });
          this.$emit('warn-black', this.blacklist.rpid);
        }
      });
    },
    dropblack() {
      Swal.fire({
        width: 350,
        text: '회원을 탈퇴시키겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#fff',
        cancelButtonColor: '#fff',
        confirmButtonText: '<a style="font-size:1rem; color:black;">탈퇴</a>',
        cancelButtonText: '<a style="font-size:1rem; color:black;">취소</a>',
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
            title: '회원을 탈퇴처리 했습니다.',
          });
          this.$emit('drop-black', this.blacklist.rpid);
        }
      });
    },
    cancelblack() {
      Swal.fire({
        width: 350,
        text: '신고를 거절 하시겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#fff',
        cancelButtonColor: '#fff',
        confirmButtonText: '<a style="font-size:1rem; color:black;">거절</a>',
        cancelButtonText: '<a style="font-size:1rem; color:black;">취소</a>',
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
            title: '신고가 거절되었습니다.',
          });

          this.$emit('cancel-black', this.blacklist.rpid);
        }
      });
    },
  },
};
</script>

<style></style>
