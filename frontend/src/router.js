import { createRouter, createWebHistory } from 'vue-router';
import AuthPage from '@/views/AuthPage.vue';
import MainPage from '@/views/MainPage.vue';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            name: 'auth',
            component: AuthPage
        },
        {
            path: '/main',
            name: 'main',
            component: MainPage
        }
    ]
});
router.beforeEach((to, from, next) => {
    const publicPages = ['/']; // Укажите здесь маршруты, которые не требуют авторизации
    const authRequired = !publicPages.includes(to.path);
    const loggedIn = sessionStorage.getItem('jwtToken');

    // Проверяем, требуется ли авторизация и авторизован ли пользователь
    if (authRequired && !loggedIn) {
        return next('/'); // Если не авторизован, перенаправляем на страницу входа
    }

    next(); // В противном случае продолжаем переход к маршруту
});

export default router;