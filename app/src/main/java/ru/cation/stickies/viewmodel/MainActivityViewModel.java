package ru.cation.stickies.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.cation.stickies.database.StickiesItemRepo;
import ru.cation.stickies.models.StickiesItem;

public class MainActivityViewModel extends AndroidViewModel {

    private final MutableLiveData<List<StickiesItem>> stickiesItemsLiveData;
    private final MutableLiveData<Boolean> loading;
    private final StickiesItemRepo repo;
    private Socket socket;

    @SuppressLint("CheckResult")
    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        stickiesItemsLiveData = new MutableLiveData<>();
        loading = new MutableLiveData<>();
        loading.setValue(false);

        repo = new StickiesItemRepo(application);

        repo
                .getAllStickiesItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stickiesItemsLiveData::postValue);

    }

    @SuppressLint("CheckResult")
    public void removeById(String id) {
        repo
                .removeStickiesItemById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @SuppressLint("CheckResult")
    public void connect(String ip) {
        Observable.fromCallable(() -> {
                    try {
                        return new Socket(ip, 54000);
                    } catch (IOException e) {
                        // Бросаем исключение в случае ошибки
                        throw new RuntimeException("Connection error: " + e.getMessage(), e);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(
                        response -> {
                            socket = response;
                            System.out.println("Connected!");
                        },
                        throwable -> System.err.println("Connection error: " + throwable.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void sync() {
        if (socket != null && socket.isConnected()) {
            loading.setValue(true);
            getListDesktop()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> {
                                findMissingLocalIDs(list);

                                // Задержка в 3 секунды перед вызовом findMissingDesktopIDs
                                Observable
                                        .just(list)
                                        .delay(3, TimeUnit.SECONDS)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(delayedList -> {
                                                    findMissingDesktopIDs(delayedList);

                                                    // Задержка в ещё 3 секунды перед вызовом updateIds
                                                    Observable
                                                            .just(delayedList)
                                                            .delay(3, TimeUnit.SECONDS)
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(
                                                                    finalList -> updateIds(finalList),
                                                                    throwable -> throwable.printStackTrace()
                                                            );
                                                },
                                                throwable -> throwable.printStackTrace());
                            },
                            throwable -> throwable.printStackTrace());


        } else {
            System.out.println("Socket is null or not connected");
        }
    }


    @SuppressLint("CheckResult")
    public void findMissingLocalIDs(List<String> list) {
        repo
                .getAllStickiesItemIds().take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(ids -> {
                    List<String> list2 = new ArrayList<>(list);
                    list2.removeAll(ids);
                    if (!Objects.equals(list2.get(0), ""))
                        downloadMissing(list2);

                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    @SuppressLint("CheckResult")
    public void findMissingDesktopIDs(List<String> list) {
        repo
                .getAllStickiesItemIds().take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(ids -> {
                    ids.removeAll(list);
                    downloadMissingDesktop(ids);
                }, throwable -> {
                });
    }

    @SuppressLint("CheckResult")
    public void updateIds(List<String> list) {
        repo
                .getAllStickiesItemIds().take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(ids -> {
                    ids.retainAll(list);
                    downloadAll(ids);
                }, throwable -> {
                });
    }

    @SuppressLint("CheckResult")
    private void downloadMissingDesktop(List<String> list) {

        Observable.fromIterable(list)
                .concatMap(id -> Observable.just(id).delay(300, TimeUnit.MILLISECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> {
                    getTextDatabaseSticky(id);
                    removeById(id);
                });
    }

    @SuppressLint("CheckResult")
    private void downloadMissing(List<String> list) {

        Observable.fromIterable(list)
                .concatMap(i -> getDesktopText(i)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .delay(300, TimeUnit.MILLISECONDS)
                )
                .subscribe();

    }

    @SuppressLint("CheckResult")
    private void downloadAll(List<String> list) {

        Observable.fromIterable(list)
                .concatMap(i -> getDesktopText(i)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .delay(300, TimeUnit.MILLISECONDS)
                )
                .subscribe(s -> loading.postValue(false), Throwable::printStackTrace);

    }

    @SuppressLint("CheckResult")
    public void doNewDatabaseSticky(String id, String text) {
        StickiesItem item = new StickiesItem(id, "test", text);
        repo
                .insertStickiesItem(item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();

    }

    @SuppressLint("CheckResult")
    public void getTextDatabaseSticky(String id) {
        repo
                .getStickiesItemById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(stickiesItem -> {
                    doNewSticky(stickiesItem.getText()).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(s -> {
                            }, throwable -> throwable.printStackTrace());
                });
    }


    public Observable<List<String>> getListDesktop() {
        return Observable.fromCallable(() -> {
            PrintWriter out;
            BufferedReader in;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println("get list desktop");
                System.out.println("get list desktop");


                String idString = in.readLine();
                idString = idString.replace("001 ", "");

                String[] idArray = idString.split("\\s*,\\s*");


                return Arrays.asList(idArray);
            } catch (IOException e) {
                throw new RuntimeException("Error sending/receiving message: " + e.getMessage(), e);
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<String> getDesktopText(String id) {
        return Observable.fromCallable(() -> {
            PrintWriter out;
            BufferedReader in;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println("get desktop " + id + " text");
                System.out.println("get desktop " + id + " text");

                String text = in.readLine();
                System.out.println(text);
                text = text.replace("001 ", "");
                text = text.replace("|||", "\n");
                doNewDatabaseSticky(id, text);
                return "";
            } catch (IOException e) {
                throw new RuntimeException("Error sending/receiving message: " + e.getMessage(), e);
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<String> doNewSticky(String text) {
        return Observable.fromCallable(() -> {
            PrintWriter out;
            BufferedReader in;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println("do new sticky " + text);
                System.out.println("do new sticky " + text);

                String response = in.readLine();
                System.out.println(response);
                doNewDatabaseSticky(parseCreatedStickyId(response), text);
                return "";
            } catch (IOException e) {
                throw new RuntimeException("Error sending/receiving message: " + e.getMessage(), e);
            }
        }).subscribeOn(Schedulers.io());
    }

    private String parseCreatedStickyId(String response) {
        String[] parts = response.split(":");
        if (parts.length >= 2) {
            return parts[1].trim();
        } else {
            throw new RuntimeException("Error parsing created sticky ID from response: " + response);
        }
    }


    public MutableLiveData<List<StickiesItem>> getStickiesItemLiveData() {
        return stickiesItemsLiveData;
    }

    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loading;
    }

}