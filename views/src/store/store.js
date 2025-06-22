import { createStore } from 'vuex';
import  defaultPic from '@/assets/defaultCover.png';
const safeParse = (key, defaultValue) => {
    const value = window.sessionStorage.getItem(key);
    try {
        return value ? JSON.parse(value) : defaultValue;
    } catch (error) {
        return defaultValue;
    }
};

const store = createStore({
    state() {
        return {
            HOST: 'http://127.0.0.1:8888',
            isPlay: safeParse('isPlay', false),
            url: safeParse('url', ''),
            name: safeParse('name', ''),
            pic: safeParse('pic', defaultPic),
            listOfSongs: safeParse('listOfSongs', []),
            playing: safeParse('playing', null),
        };
    },
    getters: {
        isPlay: (state) => state.isPlay,
        url: (state) => state.url,
        name: (state) => state.name,
        pic: (state) => state.pic,
        listOfSongs: (state) => state.listOfSongs,
        currentIndex: (state) => state.listOfSongs.findIndex(song => song.id === state.playing),
        playing: (state) => state.playing,  // 确保正确引用 playing
    },
    mutations: {
        setIsPlay(state, isPlay) {
            state.isPlay = isPlay;
            window.sessionStorage.setItem('isPlay', JSON.stringify(isPlay));
        },
        setUrl(state, url) {
            state.url = url;
            window.sessionStorage.setItem('url', JSON.stringify(url));
        },
        setName(state, name) {
            state.name = name;
            window.sessionStorage.setItem('name', JSON.stringify(name));
        },
        setPic(state, pic) {
            state.pic = pic;
            window.sessionStorage.setItem('pic', JSON.stringify(pic));
        },
        setListOfSongs(state, listOfSongs) {
            state.listOfSongs = listOfSongs;
            window.sessionStorage.setItem('listOfSongs', JSON.stringify(listOfSongs));
        },
        setPlaying(state, playing) {
            state.playing = playing;
            window.sessionStorage.setItem('playing', JSON.stringify(playing));  // 同步到 sessionStorage
        },
    },
    actions: {
        updatePlayState({ commit }, { isPlay, name, pic, url }) {
            commit('setIsPlay', isPlay);
            commit('setName', name);
            commit('setPic', pic);
            commit('setUrl', url);
        },
        updatePlaying({ commit }, playing) {
            commit('setPlaying', playing);
            localStorage.setItem('playing', playing); // 同步到 localStorage
        },
        playNextSong({ state, commit }) {
            const currentIndex = state.listOfSongs.findIndex(song => song.url === state.url);
            if (currentIndex === -1 || state.listOfSongs.length === 0) return;

            const nextIndex = (currentIndex + 1) % state.listOfSongs.length; // 循环播放
            const nextSong = state.listOfSongs[nextIndex];

            commit('setUrl', nextSong.url);
            commit('setPlaying', nextSong.id);
            commit('setPic',nextSong.pic);
            commit('setName',nextSong.name);// 更新 Vuex 中的正在播放歌曲 ID
            localStorage.setItem('playing', nextSong.id); // 同步到 localStorage
            commit('setIsPlay', true);
        },
        playPreviousSong({ state, commit }) {
            const currentIndex = state.listOfSongs.findIndex(song => song.url === state.url);
            if (currentIndex === -1 || state.listOfSongs.length === 0) return;
            const nextIndex = (currentIndex - 1) % state.listOfSongs.length; // 循环播放
            const nextSong = state.listOfSongs[nextIndex];
            commit('setUrl', nextSong.url);
            commit('setPic',nextSong.pic);
            commit('setName',nextSong.name);
            commit('setPlaying', nextSong.id);  // 更新 Vuex 中的正在播放歌曲 ID
            localStorage.setItem('playing', nextSong.id); // 同步到 localStorage
            commit('setIsPlay', true);
        },
        playSongAtIndex({ state, commit }, index) {
            if (!state.listOfSongs.length) return;
            let targetIndex = index;
            if (index === undefined || state.playMode === 'random') {
                const currentIndex = state.listOfSongs.findIndex(song => song.url === state.url);
                do {
                    targetIndex = Math.floor(Math.random() * state.listOfSongs.length);
                    console.log(targetIndex)
                } while (targetIndex === currentIndex && state.listOfSongs.length > 1);
            }

            const song = state.listOfSongs[targetIndex];
            if (!song) return;

            commit('setUrl', song.url);
            commit('setPic', song.pic);
            commit('setName', song.name);
            commit('setPlaying', song.id);
            localStorage.setItem('playing', song.id);
            commit('setIsPlay', true);
        }
    },
});

export default store;
