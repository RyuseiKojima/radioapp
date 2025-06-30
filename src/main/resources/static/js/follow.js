/**
 * フォロー機能JavaScript
 */
function initializeFollowButtons() {
    // 番組一覧ページかどうかをチェック
    if (!document.querySelector('.follow-btn')) {
        return;
    }
    
    // CSRF トークンを取得
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
    
    // 各フォローボタンの初期状態を設定
    const followButtons = document.querySelectorAll('.follow-btn');
    
    // 各ボタンに対して初期状態の設定とイベントリスナーを追加
    followButtons.forEach((button) => {
        const programId = button.getAttribute('data-program-id');
        const isFollowing = button.getAttribute('data-is-following') === 'true';
        
        // バックエンドから渡された初期状態を反映
        updateFollowButton(button, isFollowing);
        
        // クリックイベントを追加
        button.addEventListener('click', function() {
            toggleFollow(programId, button);
        });
    });
    
    function updateFollowButton(button, isFollowing) {
        const icon = button.querySelector('.follow-icon');
        const text = button.querySelector('.follow-text');
        
        // フォロー状態に応じてボタンのスタイルとテキストを更新
        if (isFollowing) {
            button.className = 'btn btn-success btn-sm w-100 position-relative follow-btn';
            icon.className = 'bi bi-heart-fill me-1 follow-icon';
            text.textContent = 'フォロー中';
        } else {
            button.className = 'btn btn-outline-success btn-sm w-100 position-relative follow-btn';
            icon.className = 'bi bi-heart me-1 follow-icon';
            text.textContent = 'フォロー';
        }
    }
    
    function toggleFollow(programId, button) {
        // ボタンを無効化
        button.disabled = true;
        
        const headers = {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        };
        
        // CSRFトークンがある場合は追加
        if (csrfToken && csrfHeader) {
            headers[csrfHeader] = csrfToken;
        }
        
        // フォロー状態をトグルするAPIリクエスト
        fetch(`/api/follow/toggle/${programId}`, {
            method: 'POST',
            headers: headers
        })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                alert('エラーが発生しました。再度お試しください。');
            } else {
                updateFollowButton(button, data.isFollowing);
            }
        })
        .catch(error => {
            console.error('フォロー状態の更新に失敗しました:', error);
            alert('エラーが発生しました。再度お試しください。');
        })
        .finally(() => {
            // ボタンを有効化
            button.disabled = false;
        });
    }
}

// DOMが読み込まれたらフォローボタンを初期化
document.addEventListener('DOMContentLoaded', function() {
    initializeFollowButtons();
});
