//package com.rolvatech.cgc.repositories;
//
//
//import android.text.TextUtils;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
////import com.rolvatech.cgc.dataaccess.CgcDao;
//import com.rolvatech.cgc.utils.AppExecutors;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import okhttp3.Response;
//
///**
// * Created by admin on 9/28/2017.
// */
//
//public class ConfigDataDownloadRepository implements IConfigDataDownloadRepository {
//
//
//    private static ConfigDataDownloadRepository INSTANCE;
//
//    private CgcDao mCgcDao;
//
//    private AppExecutors mAppExecutors;
//
//    private OnLoadDataCallBack onLoadDataCallBack;
//
//    private int TOTAL_NO_OF_CALLBACKS = 1;
//
//    private int EXECUTED_NO_OF_CALLBACKS = 0;
//
//    private String BACK_SLASH = "/";
//
//    private String NOT_FOUND = "Not Found";
//
//    private ConfigDataDownloadRepository(@NonNull AppExecutors appExecutors, CgcDao cgcDao) {
//        mAppExecutors = appExecutors;
//        mCgcDao = cgcDao;
//    }
//
//    public static ConfigDataDownloadRepository getInstance(@NonNull AppExecutors appExecutors,
//                                                           @NonNull CgcDao cgcDao) {
//        if (INSTANCE == null) {
//            synchronized (ConfigDataDownloadRepository.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = new ConfigDataDownloadRepository(appExecutors, cgcDao);
//                }
//            }
//        }
//        return INSTANCE;
//    }
//
//
//    @Override
//    public void downloadConfigData(final OnLoadDataCallBack onLoadDataCallBack) {
//
//        EXECUTED_NO_OF_CALLBACKS = 0;
//
//        this.onLoadDataCallBack = onLoadDataCallBack;
//
//        downloadIncidentTypes();
//
//
//    }
//
//
//    @Override
//    public void deleteConfigData(OnLoadDataCallBack onLoadDataCallBack) {
//
//    }
//
//    private void parseResponse(final Response response,
//                               final String method, final String identifier) {
//        Runnable saveRunnable = new Runnable() {
//            JSONObject jsonObject = null;
//            JSONArray jsonArray = null;
//            JSONArray jsonObjArray = new JSONArray();
//
//            @Override
//            public void run() {
//                try {
//                    if (response.isSuccessful()) {
//                        String s = response.body().string();
//
//                        jsonObject = new JSONObject(s);
//
//                        if (!TextUtils.isEmpty(identifier)) {
//                            Log.e("res2", jsonObject.toString());
//                            jsonArray = jsonObject.optJSONArray(identifier);
//                            //if (jsonObject.getJSONObject("serverResponse").get("statusMsg").equals("SUCCESSFUL")) {
//                            if (jsonArray == null && jsonObject.getJSONObject("serverResponse") != null) {
//                                jsonArray = jsonObject.getJSONObject("serverResponse")
//                                        .optJSONArray(identifier);
//                                if (jsonArray == null && (!jsonObject.getJSONObject("serverResponse").get("statusText").equals(NOT_FOUND))
//                                        && null != jsonObject.getJSONObject("serverResponse").getJSONObject(identifier)) {
//                                    jsonArray = jsonObjArray.put(jsonObject.getJSONObject("serverResponse").getJSONObject(identifier));
//                                }
//                            }
//                        }
//
//                        if (jsonArray == null) {
////                            if (jsonObject.getJSONObject("serverResponse").get("statusText").equals(NOT_FOUND)) {
////                                switch (method) {
////                                    case INVENTORY:
////                                        trackTraceDao.deleteInventoryLocalData();
////                                        break;
////                                    case INVENTORY_CONTAINER:
////                                        trackTraceDao.deleteInventoryContainerLocalData();
////                                        break;
////                                    case INVENTORY_PALLET:
////                                        trackTraceDao.deleteInventoryPalletLocalData();
////                                        break;
////                                    case INVENTORY_LOAD:
////                                        trackTraceDao.deleteInventoryLoadLocalData();
////                                        break;
////                                }
////                            }
//
//                        } else {
//
//                            switch (method) {
////                                case SITE:
////                                    Log.e("res1", jsonObject.toString());
////                                    PrefUtils.setStringPreference(IFLApplication.app(),
////                                            Constants.Pref.DEFAULT_INCIDENT, jsonObject.
////                                                    optJSONObject("site").optString("incidentTypeCode"));
////                                    PrefUtils.setStringPreference(IFLApplication.app(),
////                                            Constants.Pref.UPDATEDON, jsonObject.
////                                                    optJSONObject("site").optString("updatedOn"));
////                                    PrefUtils.setStringPreference(IFLApplication.app(),
////                                            Constants.Pref.DEFAULT_OTHER_ACTIVITY, jsonObject.
////                                                    optJSONObject("site").optString("otherActivityCode"));
////                                    PrefUtils.setStringPreference(IFLApplication.app(),
////                                            Constants.Pref.DEFAULT_HANDLING_ACTIVITY, jsonObject.
////                                                    optJSONObject("site").optString("handlingActivityCode"));
////                                    PrefUtils.setBooleanPreference(IFLApplication.app(),
////                                            PrefUtils.ALLOW_OFFLINE, jsonObject.
////                                                    optJSONObject("site").optBoolean("allowOffline"));
////                                    PrefUtils.setStringPreference(IFLApplication.app(),
////                                            PrefUtils.MINIMUM_CONNECTION,
////                                            jsonObject.optJSONObject("site").optString("minimumConnection"));
////                                    break;
////                                case INVENTORY:
////                                    List<Inventory> lsContainerContents = asList(new Gson().fromJson(jsonArray.toString(), Inventory[].class));
////                                    trackTraceDao.deleteInventoryQuantityZero();
////                                    for (Inventory inventory : lsContainerContents) {
////                                        String containerCode = trackTraceDao.findInventoryCodeById(inventory.getInventoryContainerId());
////                                        InventoryContainer containerCont = trackTraceDao.findInventoryContainerContentByContainerAndClientItemRef(
////                                                containerCode, inventory.getEquipmentNumber(), inventory.getProductionRef(), inventory.getProductId(), inventory.getLotCode(), inventory.getExpiryDate(), inventory.getCevaOrderId());
////                                        if (containerCont != null) {
////                                            boolean hasPushed = hasAllDataPushed(containerCont.getContainerCode());
////
////                                            if (hasPushed) {
////                                                trackTraceDao.updateInventoryContainerContent(inventory.getQuantity(),
////                                                        inventory.getInventoryContainerId(), inventory.getProductId(), inventory.getLocationId(),
////                                                        inventory.getEquipmentNumber(), inventory.getProductionRef(), inventory.getLotCode(), inventory.getExpiryDate(), inventory.getCevaOrderId());
////                                            }
////                                        } else {
////                                            if (inventory.getProductId() != 0)
////                                                inventory.setProduct(mConfigDao.getProducts(inventory.getId()));
////                                            inventory.setIsDevice(0);
////                                            trackTraceDao.insertInventory(inventory);
////                                        }
////                                    }
////                                    trackTraceDao.deleteInventoryLocalData();
////                                    break;
////                                case INVENTORY_CONTAINER:
////                                    List<InventoryContainer> inventoryContainers = Arrays.asList(new Gson().fromJson(jsonArray.toString(), InventoryContainer[].class));
////                                    for (InventoryContainer inventoryContainer : inventoryContainers) {
////                                        if (String.valueOf(inventoryContainer.getInventoryPalletId()) == null) {
////                                            inventoryContainer.setInventoryPalletId(-1);
////                                        }
////                                        if (trackTraceDao.isInventoryContainerExists(inventoryContainer.getContainerCode()) > 0) {
////                                            trackTraceDao.deleteInventoryContainer(inventoryContainer.getContainerCode());
////                                        }
////                                        inventoryContainer.setIsDevice(0);
////                                        trackTraceDao.insertInventoryContainer(inventoryContainer);
////                                    }
////                                    trackTraceDao.deleteInventoryContainerLocalData();
////                                    break;
////                                case INVENTORY_LOAD:
////                                    List<InventoryLoad> inventoryLoad = Arrays.asList(new Gson().fromJson(jsonArray.toString(), InventoryLoad[].class));
////                                    for (InventoryLoad inventoryLoad1 : inventoryLoad) {
////                                        inventoryLoad1.setIsDevice(0);
////                                        int count = mConfigDao.hasLoadExists(inventoryLoad1.getTrailerRef());
////                                        if (count > 0) {
////                                            trackTraceDao.updateInventoryLoadContent(inventoryLoad1.getId(), inventoryLoad1.getTrailerRef(), inventoryLoad1.getUpdatedOn());
////                                        } else {
////                                            trackTraceDao.insertInventoryLoad(inventoryLoad);
////                                        }
////                                    }
////                                    trackTraceDao.deleteInventoryLoadLocalData();
////
////                                    break;
////                                case INVENTORY_SERIAL:
////                                    List<InventorySerial> inventorySerials = Arrays.asList(new Gson().fromJson(jsonArray.toString(), InventorySerial[].class));
////                                    for (InventorySerial inventorySerial : inventorySerials) {
////                                        trackTraceDao.insertInventorySerial(inventorySerial);
////                                    }
////                                    break;
////                                case INVENTORY_PALLET:
////
////                                    List<InventoryPallet> inventoryPallets = Arrays.asList(new Gson().fromJson(jsonArray.toString(), InventoryPallet[].class));
////                                    for (InventoryPallet inventoryPallet : inventoryPallets) {
////                                        int count = mConfigDao.hasPalletExists(inventoryPallet.getPalletCode());
////                                        if (count > 0) {
////                                            trackTraceDao.updateInventoryPalletContent(inventoryPallet.getId(), inventoryPallet.getInventoryLoadId(), inventoryPallet.getPalletCode(), inventoryPallet.getUpdatedOn(), inventoryPallet.getUpdatedBy());
////                                        } else {
////                                            inventoryPallet.setIsDevice(0);
////                                            trackTraceDao.insertInventoryPallet(inventoryPallet);
////                                        }
////                                    }
////                                    trackTraceDao.deleteInventoryPalletLocalData();
////
////                                    break;
////                                case INCIDENTS:
////                                    List<Incidents> lsIncidents = Arrays.asList(new Gson().fromJson(jsonArray.toString(), Incidents[].class));
////                                    mConfigDao.insertIncidents(lsIncidents);
////                                    break;
////                                case LOCATION:
////                                    List<Location> lsLocations = Arrays.asList(new Gson().
////                                            fromJson(jsonArray.toString(), Location[].class));
////                                    mConfigDao.insertLocations(lsLocations);
////                                    break;
////                                case SHIPMENT:
////                                    List<Shipment> lsShipments = Arrays.asList(new Gson().
////                                            fromJson(jsonArray.toString(), Shipment[].class));
////                                    mConfigDao.insertShipments(lsShipments);
////                                    break;
//////                                case SHIPMENT_STATUS:
//////                                    List<ShipmentStatus> lsShipmentStatus = Arrays.asList(new Gson().fromJson(jsonArray.toString(), ShipmentStatus[].class));
//////                                    mConfigDao.insertShipmentStatus(lsShipmentStatus);
//////                                    break;
//////                                case DELIVERY_TYPE:
//////                                    List<RepackDeliveryTypes> lsDeliveryTypes = Arrays.asList(new Gson().
//////                                            fromJson(jsonArray.toString(), RepackDeliveryTypes[].class));
//////                                    mConfigDao.insertRepackDeliveryTypes(lsDeliveryTypes);
//////                                    break;
////                                case INCIDENT_TYPES:
////                                    List<IncidentTypes> lsIncidentTypes = Arrays.asList(new Gson().fromJson(jsonArray.toString(), IncidentTypes[].class));
////                                    CevaLog.v("lsIncidentTypes..", lsIncidentTypes.toString());
////                                    mConfigDao.insertIncidentTypes(lsIncidentTypes);
////                                    break;
////                                case OBJECTS:
////                                    List<ObjectPojo> objectPojoList = Arrays.asList(new Gson().fromJson(jsonArray.toString(), ObjectPojo[].class));
////                                    mConfigDao.insertObjects(objectPojoList);
////                                    break;
////                                case INCIDENT_REASONS:
////                                    List<IncidentReasons> lsIncidentReasons = (List<IncidentReasons>) IncidentReasons.
////                                            parseResponse(jsonArray);
////                                    mConfigDao.insertIncidentReasons(lsIncidentReasons);
////                                    break;
////                                case STATION:
////                                    List<Station> lsStations = asList(new Gson().fromJson(jsonArray.toString(), Station[].class));
////                                    mConfigDao.insertStations(lsStations);
////                                    break;
////                                case PROGRAM:
////                                    List<Program> lsPrograms = asList(new Gson().fromJson(jsonArray.toString(), Program[].class));
////                                    mConfigDao.insertPrograms(lsPrograms);
////                                    break;
////                                case SITE_ACTIVITY:
////                                    List<SiteActivity> lsSiteActivity = (List<SiteActivity>) SiteActivity.parseJson(jsonArray);
////
////                                    mConfigDao.insertSiteActivities(lsSiteActivity);
////                                    break;
////                                case PRODUCT_FAMILY:
////                                    List<ProductFamily> lsProductFamilies = asList(new Gson().fromJson(jsonArray.toString(), ProductFamily[].class));
////                                    mConfigDao.insertProductFamily(lsProductFamilies);
////                                    break;
////                                case PRODUCT:
////                                    CevaLog.v("product jsonArray..", jsonArray.toString());
////                                    List<Product> lsProducts = (List<Product>) Product.parseJson(jsonArray);
////                                    mConfigDao.insertProducts(lsProducts);
////                                    break;
////                                case SITE_OPERATORS:
////                                    List<SiteOperators> lsSiteOperators = asList(new Gson().fromJson(jsonArray.toString(), SiteOperators[].class));
//////                                    for(SiteOperators siteOperators: lsSiteOperators)
//////                                    {
//////                                        siteOperators.setLocation(f);
//////                                    }
////                                    mConfigDao.insertSiteOperators(lsSiteOperators);
////                                    break;
////                                case KANBAN_BIN_INFO:
////
////                                    List<KanbanBin> kanbanBinList = asList(new Gson().fromJson(jsonArray.toString(), KanbanBin[].class));
////                                    for (KanbanBin kanbanBin : kanbanBinList) {
////                                        kanbanBin.setLocationCode(trackTraceDao.findLocationCodeById(kanbanBin.getLocationId()));
////                                        kanbanBin.setProductCode(kanbanDao.findProductCodeById(kanbanBin.getProductId()));
////                                        kanbanBin.setBinType(kanbanDao.findBinTypecodeByBinId(kanbanBin.getTypeId()));
////                                        kanbanBin.setStatusCode(kanbanDao.findStatusCodeById(kanbanBin.getStatusId()));
////                                        kanbanBin.setStationCode(kanbanDao.findStationCodeById(kanbanBin.getStationId()));
////                                        kanbanDao.insertKanbanBins(kanbanBin);
////                                    }
////                                    break;
////                                case USER_LOGIN_DETAILS:
////                                    List<UserLocationPojo> userLocationPojoList = asList(new Gson().fromJson(jsonArray.toString(), UserLocationPojo[].class));
////                                    mConfigDao.insertUserInfo(userLocationPojoList);
////                                    break;
////                                case AREA_TRAVEL:
////                                    List<AreaTravelRestPojo> lsAreaTravelRestPojo = (List<AreaTravelRestPojo>) AreaTravelRestPojo.parseJson(jsonArray);
////                                    mConfigDao.insertAreaTravel(lsAreaTravelRestPojo);
////                                    break;
////                                case STATION_TRAVEL:
////                                    List<StationTravelRestPojo> stationTravelRestPojos = asList(new Gson().fromJson(jsonArray.toString(), StationTravelRestPojo[].class));
////                                    mConfigDao.insertStationTravel(stationTravelRestPojos);
////                                    break;
//////                                case ROLES:
//////                                    List<RoleVO> roleVOS = asList(new Gson().fromJson(jsonArray.toString(), RoleVO[].class));
//////                                    mConfigDao.deleteRoles();
//////                                    mConfigDao.insertRoles(roleVOS);
//////                                    downloadAllRolesPermission();
//////                                    break;
////                                case PERMISSIONS:
////                                    List<PermissionVO> permissionVOS = asList(new Gson().fromJson(jsonArray.toString(), PermissionVO[].class));
////                                    mConfigDao.insertPermissions(permissionVOS);
////                                    break;
////                                case LOCATION_TRAVEL:
////                                    List<LocationTravelRestPojo> locationTravelRestPojos = asList(new Gson().fromJson(jsonArray.toString(), LocationTravelRestPojo[].class));
////                                    mConfigDao.insertLocationTravel(locationTravelRestPojos);
////                                    break;
////                                case ACTIVITY_WORK_TIME:
////                                    List<ActivityWorkTimePojo> activityWorkTimePojos = asList(new Gson().fromJson(jsonArray.toString(), ActivityWorkTimePojo[].class));
////                                    mConfigDao.insertActivityWorkTime(activityWorkTimePojos);
////                                    break;
////                                case CEVA_ORDER:
////                                    List<CevaOrderVo> cevaOrderVos = asList(new Gson().fromJson(jsonArray.toString(), CevaOrderVo[].class));
////                                    mConfigDao.insertCevaOrder(cevaOrderVos);
////                                    break;
//////                                case CEVA_ORDER_STATUS:
//////                                    List<CevaOrderStatus> cevaOrderStatuses = asList(new Gson().fromJson(jsonArray.toString(), CevaOrderStatus[].class));
//////                                    mConfigDao.insertCevaOrderStatus(cevaOrderStatuses);
//////                                    break;
////                                case CHECKS:
////                                    List<ChecksRest> lsChecksRest = asList(new Gson().fromJson(jsonArray.toString(), ChecksRest[].class));
////                                    mConfigDao.insertChecks(lsChecksRest);
////                                    break;
////                                case CHECK_LIST_DATA:
////                                    List<CheckListRest> lsCheckListRest = asList(new Gson().fromJson(jsonArray.toString(), CheckListRest[].class));
////                                    mConfigDao.insertCheckList(lsCheckListRest);
////                                    break;
////                                case CHECK_LIST_DETAILS:
////                                    List<CheckListDetailRest> lsCheckListDetailRest = asList(new Gson().fromJson(jsonArray.toString(), CheckListDetailRest[].class));
////                                    mConfigDao.insertCheckListDetail(lsCheckListDetailRest);
////                                    break;
////                                case CHECK_LIST_SETUP:
////                                    List<CheckListSetupRest> lsCheckListSetupRest = asList(new Gson().fromJson(jsonArray.toString(), CheckListSetupRest[].class));
////                                    mConfigDao.insertCheckListSetup(lsCheckListSetupRest);
////                                    break;
////
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    Log.e("sync data exception..", e.getMessage(), e.getCause());
//                } finally {
//                    onDataDownloaded();
//                }
//            }
//        };
//
//        mAppExecutors.diskIO().execute(saveRunnable);
//
//    }
//
//
//    private void downloadIncidentTypes() {
//
////        String onUpdatedDate = mConfigDao.findIncidentUpdatedOn();
////        String url = SERVER_URL + INCIDENT_TYPES + PrefUtils.getStringPreference(IFLApplication.app(), PrefUtils.SITE_ID) + BACK_SLASH + onUpdatedDate;
////        CevaLog.v("incident Types. ULR...", url);
////        NetworkRequestModel model = new NetworkRequestModel();
////        model.setMethod(GET);
////        model.setUrl(url);
////
////        ApiRequest.getInstance().setRequestModel(model)
////                .callApi(new ApiResponseListener() {
////
////                    @Override
////                    public void onResponse(Call call, final Response response) {
////                        parseResponse(response, INCIDENT_TYPES, "incidentTypes");
////                    }
////
////                    @Override
////                    public void onFailure(Call call, IOException e) {
////                        onDataDownloadFailure();
////                    }
////
////                });
//    }
//
//
//    private void onDataDownloaded() {
//        EXECUTED_NO_OF_CALLBACKS++;
//
//        if (EXECUTED_NO_OF_CALLBACKS == TOTAL_NO_OF_CALLBACKS) {
//            onLoadDataCallBack.onDataDownloaded();
//        }
//    }
//
//    private void onDataDownloadFailure() {
//        onLoadDataCallBack.onDataDownloadFailure();
//    }
//}
//
