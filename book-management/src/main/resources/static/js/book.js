/**
 * 도서 관리 시스템 JavaScript
 * jQuery + Ajax를 활용한 REST API 호출 예시 포함
 */

$(document).ready(function () {

    /* =====================================================
       1. REST API 호출 예시 (면접 시 Ajax 동작 설명용)
          개발자 도구 콘솔에서 loadBooksViaApi() 로 직접 호출 가능
       ===================================================== */

    /**
     * GET /api/books — 도서 목록을 비동기로 조회하여 콘솔에 출력
     */
    window.loadBooksViaApi = function (params) {
        $.ajax({
            url: '/api/books',
            type: 'GET',
            data: params || {},         // 예: { title: '자바', page: 1 }
            dataType: 'json',
            success: function (response) {
                console.group('[API] 도서 목록 응답');
                console.log('총 건수     :', response.totalElements);
                console.log('전체 페이지 :', response.totalPages);
                console.log('현재 페이지 :', response.currentPage);
                console.log('도서 목록   :', response.content);
                console.groupEnd();
            },
            error: function (xhr, status, error) {
                console.error('[API] 도서 목록 조회 실패:', status, error);
            }
        });
    };

    /**
     * GET /api/books/{id} — 특정 도서를 비동기로 조회
     */
    window.getBookById = function (id) {
        $.ajax({
            url: '/api/books/' + id,
            type: 'GET',
            dataType: 'json',
            success: function (book) {
                console.group('[API] 도서 상세 응답');
                console.log(book);
                console.groupEnd();
            },
            error: function (xhr) {
                console.error('[API] 오류:', xhr.responseJSON);
            }
        });
    };

    /* =====================================================
       2. Bootstrap 5 알림 자동 닫기 (3초 후)
       ===================================================== */
    var successAlert = document.querySelector('.alert-success');
    if (successAlert) {
        setTimeout(function () {
            var bsAlert = new bootstrap.Alert(successAlert);
            bsAlert.close();
        }, 3000);
    }

    /* =====================================================
       3. 숫자 입력 필드 — 음수 방지
       ===================================================== */
    $('input[type="number"][min="0"]').on('input', function () {
        var val = parseInt($(this).val(), 10);
        if (isNaN(val) || val < 0) {
            $(this).val(0);
        }
    });

    /* =====================================================
       4. 삭제 확인 (data-confirm 속성 범용 처리)
          사용 예: <button data-confirm="정말 삭제?">삭제</button>
       ===================================================== */
    $('[data-confirm]').on('click', function (e) {
        var message = $(this).data('confirm') || '정말 진행하시겠습니까?';
        if (!confirm(message)) {
            e.preventDefault();
        }
    });

});
